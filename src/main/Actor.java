package main;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import util.math.Vec;

import java.util.*;
import java.util.logging.Logger;

import static util.math.Convenience.scaleDelta;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public abstract class Actor implements IAnimatable, IEntity, ICombatable {
  protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  /**
   * MovementState switches between different movement-characteristics
   * of the actor.
   * CAN_MOVE: the actor can move normally.
   * IS_KNOCKED_BACK: the actor is currently being knocked back and can not be moved by input.
 */
  protected enum MovementState {
    CAN_MOVE,
    IS_KNOCKED_BACK,
    SUSPENDED
  };

  protected Point position;
  protected DungeonWorld level;

  protected Animation idleAnimationRight;
  protected Animation idleAnimationLeft;
  protected Animation runAnimationLeft;
  protected Animation runAnimationRight;
  protected Animation hitAnimation;
  protected Animation currentAnimation;
  private enum AnimationState {
    IDLE,
    RUN,
    KNOCK_BACK,
    HIT
  }

  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;

  /**
   * The current MovementState of the actor.
   * @see MovementState
   */
  protected MovementState movementState;

  /**
   * Defines whether the actor can be knocked back or not.
   */
  protected boolean knockBackAble = false;

  /**
   * Gets the value of knockBackAble.
   * @return The value of knockBackAble.
   */
  protected boolean isKnockBackAble() {
    return knockBackAble;
  }

  /**
   * The target point for the current knock back.
   */
  protected Point knockBackTargetPoint;
  /**
   * The speed for being knocked back. Gets added to the position of the
   * actor every update, if it is being knocked back.
   */
  protected float knockBackSpeed = 0.25f;

  /**
   * The distance the actor should be knocked back.
   */
  protected float knockBackDistance = 0.8f;

  // implementation of ICombatable -----------------------------------------------------------------------------------

  private final Timer attackTimer;
  protected long attackDelay = 1000;
  protected boolean canAttack;
  protected float movementSpeed = 0.1f;
  // cache a reference to the game to be able to scan all entities for possible attack targets
  public static Game game;
  private ICombatable target;

  // combat-characteristics:
  //TODO:acter should only have basic hit chance, attack damge, evasion change
  //since it is used for the monster as well which doesnt can upgrade it's skills
  protected float health = 100.f;
  protected float maxHealth = 100.f;

  protected float baseHitChance = 0.6f;
  protected float hitChanceModifierScroll = 1.f;
  protected float hitChanceModifierWeapon = 1.f;

  protected float baseAttackDamage = 50;
  protected float attackDamageModifierScroll = 1.f;
  protected float attackDamageModifierWeapon= 1.f;

  protected float baseEvasionChance = 0.15f;
  protected float evasionChanceModifierScroll = 1.f;
  protected float evasionChanceModifierWeapon = 1.f;

  @Override
  public float getHealth() {
    return this.health;
  }
  @Override
  public void setHealth(float health) {
    this.health = health;
  }
  @Override
  public boolean isPassive() {
    return false;
  }
  @Override
  public boolean hasTarget() {
    return this.target != null;
  }
  @Override
  public ICombatable getTarget() {
    return this.target;
  }
  @Override
  public void setTarget(ICombatable target) {
    this.target = target;
  }
  @Override
  public boolean isOtherFriendly(ICombatable other) {
    return false;
  }

  @Override
  public float getHitChance() { return baseHitChance * hitChanceModifierWeapon * hitChanceModifierScroll; }

  @Override
  public float getEvasionChance() { return this.baseEvasionChance * evasionChanceModifierWeapon * evasionChanceModifierScroll; }
  @Override
  public float getDamage() { return this.baseAttackDamage * this.attackDamageModifierWeapon * this.attackDamageModifierScroll; }
  @Override
  public float attack(ICombatable other) {
    float damage = ICombatable.super.attack(other);
    animationState = AnimationState.HIT;
    // delay next attack by attackDelay ms
    this.canAttack = false;
    TimerTask resetCanAttackTask = new TimerTask() {
      @Override
      public void run() {
        canAttack = true;
      }
    };
    attackTimer.schedule(resetCanAttackTask, attackDelay);
    return damage;
  }
  @Override
  public boolean canAttack() {
    return ICombatable.super.canAttack() && canAttack;
  }

  @Override
  public void dealDamage(float damage, ICombatable attacker) {
    ICombatable.super.dealDamage(damage, attacker);

    if (isKnockBackAble() && attacker != null) {
      initiateKnockBack(attacker);
    }
  }
  @Override
  public void heal(float amount) {
    //TODO: negative amount on poison?
    this.health += amount;
    if (this.health > maxHealth) {
      this.health = maxHealth;
    }
  }

  /**
   * Set this.lookLeft according to position of target.
   */
  protected void lookAtTarget() {
    if (this.target instanceof IDrawable) {
      Point targetPosition = ((IDrawable)this.target).getPosition();
      if (this.position.x > targetPosition.x) {
        lookLeft = true;
      } else if (this.position.x < targetPosition.x) {
        lookLeft = false;
      }
    }
  }
  // end ICombatable implementation ----------------------------------------------------------------

  /**
   * Starts a knock back and calculates the knockBackTargetPoint.
   * @param other The attacker, which caused the knock back. Used to calculate the destination
   *              in which the knock back should be performed (the opposite of the difference vector
   *              of this.position and other.position). Should implement IDrawable.
   */
  protected void initiateKnockBack(ICombatable other) {
    if (other instanceof IDrawable) {
      var attackerPosition = ((IDrawable)other).getPosition();

      var diff = scaleDelta(this.position, attackerPosition, knockBackDistance);
      this.knockBackTargetPoint = (new Vec(this.position)).subtract(diff).toPoint();
      this.movementState = MovementState.IS_KNOCKED_BACK;
    }
  }

  /**
   * Calculate the target point for the knock back in this frame (should be called in update).
   * Ends the knock back, if the magnitude of the difference vector is less than the
   * knockBackSpeed (otherwise the actor may overshoot the knockBackTargetPoint, which can lead
   * to oscillation around the knockBackTargetPoint).
   * Ends the knock back, if the calculated target point lies in a tile, which is not accessible.
   * @return The target point for a knock back in the current frame.
   */
  protected Point calculateKnockBackTarget() {
    var positionVec = new Vec(this.position);
    var knockBackTargetVec = new Vec(this.knockBackTargetPoint);

    var diffMagnitude = positionVec.subtract(knockBackTargetVec).magnitude();
    if (diffMagnitude < knockBackSpeed) {
      movementState = MovementState.CAN_MOVE;
      return this.position;
    } else {
      var normalizedDiff = scaleDelta(this.position, this.knockBackTargetPoint, knockBackSpeed);
      var targetPoint = positionVec.add(normalizedDiff).toPoint();

      if (!level.isTileAccessible(targetPoint)) {
        movementState = MovementState.CAN_MOVE;
        return this.position;
      } else {
        return targetPoint;
      }
    }
  }

  /**
   * Constructor of the Actor class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Actor() {
    this.game = Game.getInstance();
    generateAnimations();
    lookLeft = false;
    canAttack = true;
    attackTimer = new Timer();
    movementState = MovementState.CAN_MOVE;
  }

  protected void generateAnimations(){
    String[] idleLeftFrames = new String[]{
            "tileset/default/default_anim.png",
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[]{
            "tileset/default/default_anim.png",
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[]{
            "tileset/default/default_anim.png",
    };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames = new String[]{
            "tileset/default/default_anim.png",
    };
    runAnimationRight = createAnimation(runRightFrames, 4);

    String[] hitAnimationFrames = new String[]{
            "tileset/default/default_anim.png"
    };
    hitAnimation = createAnimation(hitAnimationFrames, 3);
  }
  /**
   *
   * @param texturePaths array of textures that should be added to the animation
   * @param frameTime time between two textures
   * @return
   */
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
    }
    return new Animation(textureList, frameTime);
  }

  /**
   * Determine the active animation which should be played.
   *
   * @return The active animation.
   */
  @Override
  public Animation getActiveAnimation() {
    return this.currentAnimation;
  }

  private void setCurrentAnimation(AnimationState animationState) {
    // TODO: play hit animation on knockback
    switch (animationState) {
      case RUN:
        this.currentAnimation = lookLeft ? this.runAnimationLeft : this.runAnimationRight;
        break;
        //TODO - Animation for left and right
      case HIT:
        this.currentAnimation = this.hitAnimation;
        break;
      case IDLE:
      default:
        this.currentAnimation = lookLeft ? this.idleAnimationLeft : this.idleAnimationRight;
    }
  }

  /**
   * Get the current position in the DungeonWorld.
   *
   * @return the current position in the DungeonWorld.
   */
  @Override
  public Point getPosition() {
    return position;
  }

  AnimationState animationState = AnimationState.IDLE;

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    animationState = AnimationState.IDLE;
    switch (movementState) {
      case CAN_MOVE:
        var movementDelta = calculateMovementDelta();
        var newPosition = (movementDelta.add(new Vec(this.position))).toPoint();

        if (level.isTileAccessible(newPosition)) {
          this.position = newPosition;

          // is the actor moving?
          if (movementDelta.magnitude() > 0.0f) {
            animationState = AnimationState.RUN;
          }
        }

        // set look direction
        if (hasTarget()) {
          lookAtTarget();
        } else if(movementDelta.x()<0){
          lookLeft=true;
        }
        else if(movementDelta.x()>0){
          lookLeft=false;
        }

        if (readCombatInput()) {
          attackTargetIfReachable(this.position, level, game.getAllEntities());
        }

        if (readPickupInput()){
          handleItemPicking();
        }
        break;
      case IS_KNOCKED_BACK:
        this.position = calculateKnockBackTarget();
        animationState = AnimationState.KNOCK_BACK;
        break;
      case SUSPENDED:
        break;
    }
    setCurrentAnimation(animationState);
    this.draw();
  }

  /**
   * Override IEntity.deletable and return false for the actor.
   *
   * @return false
   */
  @Override
  public boolean deleteable() {
    return false;
  }

  /**
   * Rests tje combat stats of the actor. E.g. after the actor died.
   */
  protected void resetCombatStats() {
    this.setHealth(maxHealth);
    this.movementState = MovementState.CAN_MOVE;
    this.canAttack = true;
  }

  /**
   * Set reference to DungeonWorld and spawn player at random position in the level.
   */
  public void setLevel(DungeonWorld level) {
    this.resetCombatStats();
    this.level = level;
    findRandomPosition();
  }

  /**
   * Sets the current position of the Hero to a random position inside the DungeonWorld.
   */
  public void findRandomPosition() {
    this.position = new Point(level.getRandomPointInDungeon());
  }

  private Vec calculateMovementDelta() {
    Point newPosition = readMovementInput();
    // calculate normalized delta of this.position and the calculated
    // new position to avoid increased diagonal movement speed
    return scaleDelta(this.position, newPosition, movementSpeed);
  }

  /**
   * abstract method which has to be overwritten in the sub classes
   * this makes it possible to control an actor autoamticly or via key presisng
   * @return returns the new point where the actor should be moved
   */
  protected abstract Point readMovementInput();
  protected boolean readCombatInput() { return true; }
  protected boolean readPickupInput() { return false; }
  protected void handleItemPicking(){}

}