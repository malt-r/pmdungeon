package main;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import java.util.*;
import java.util.logging.Logger;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public abstract class Actor implements IAnimatable, IEntity, ICombatable {
  protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  protected enum MovementState {
    CAN_MOVE,
    IS_KNOCKED_BACK
  };

  protected Point position;
  protected DungeonWorld level;

  protected Animation idleAnimationRight;
  protected Animation idleAnimationLeft;
  protected Animation runAnimationLeft;
  protected Animation runAnimationRight;
  protected Animation currentAnimation;
  private enum AnimationState {
    IDLE,
    RUN,
    KNOCK_BACK,
  }

  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;

  protected MovementState movementState;
  protected boolean knockBackAble = false;
  protected boolean isKnockBackAble() {
    return knockBackAble;
  }
  protected Point knockBackTargetPoint;
  protected float knockBackSpeed = 0.25f;
  protected float knockBackDistance = 0.8f;

  // implementation of ICombatable -----------------------------------------------------------------------------------

  private final Timer attackTimer;
  protected long attackDelay = 1000;
  protected boolean canAttack;
  protected float movementSpeed = 0.1f;
  // cache a reference to the game to be able to scan all entities for possible attack targets
  private final Game game;
  private ICombatable target;

  // combat-characteristics:
  //TODO:acter should only have basic hit chance, attack damge, evasion change
  //since it is used for the monster as well which doesnt can upgrade it's skills
  protected float health = 100.f;
  protected float maxHealth = 100.f;

  protected float baseHitChance = 0.6f;
  protected float hitChanceModifier = 1.f;

  protected float baseAttackDamage = 50;
  protected float attackDamageModifier = 1.f;

  protected  float baseEvasionChance = 0.15f;
  protected float evasionChanceModifier = 1.f;

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
  public float getHitChance() {
    return baseHitChance * hitChanceModifier;
  }

  @Override
  public float getEvasionChance() {
    return this.baseEvasionChance * evasionChanceModifier;
  }
  @Override
  public float getDamage() {
    return this.baseAttackDamage * this.attackDamageModifier;
  }
  @Override
  public float attack(ICombatable other) {
    float damage = ICombatable.super.attack(other);
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
    return canAttack;
  }

  @Override
  public void dealDamage(float damage, ICombatable attacker) {
    ICombatable.super.dealDamage(damage, attacker);

    if (isKnockBackAble()) {
      initiateKnockBack(attacker);
    }
  }
  @Override
  public void heal(float amount) {
    this.health += amount;
    if (this.health > maxHealth) {
      this.health = maxHealth;
    }
  }
  // end ICombatable implementation ----------------------------------------------------------------

  protected void initiateKnockBack(ICombatable other) {
    if (other instanceof IDrawable) {
      var attackerPosition = ((IDrawable)other).getPosition();

      var diff = normalizeDelta(this.position, attackerPosition, knockBackDistance);
      this.knockBackTargetPoint = new Point(this.position.x - diff.x, this.position.y - diff.y);
      this.movementState = MovementState.IS_KNOCKED_BACK;
    }
  }

  protected Point calculateKnockBackTarget() {
    var diffMagnitude =
            Math.sqrt
              (
                (Math.pow((double)this.position.x - (double)this.knockBackTargetPoint.x, 2.f)) +
                (Math.pow((double)this.position.y - (double)this.knockBackTargetPoint.y, 2.f))
              );
    if (diffMagnitude < knockBackSpeed) {
      movementState = MovementState.CAN_MOVE;
      return this.position;
    } else {
      var normalizedDiff = normalizeDelta(this.position, this.knockBackTargetPoint, knockBackSpeed);
      var targetPoint = new Point(this.position.x + normalizedDiff.x, this.position.y + normalizedDiff.y);

      if (!level.isTileAccessible(targetPoint)) {
        movementState = MovementState.CAN_MOVE;
        return this.position;
      } else {
        return targetPoint;
      }
    }
  }

  /**
   * Constructor of the Hero class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Actor(Game game) {
    this.game = game;
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
  }

  private Animation createAnimation(String[] texturePaths, int frameTime) {
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

  /**
   * Normalize the difference-vector between two Points on a defined basis.
   *
   * @param p1                 The origin of the vector
   * @param p2                 The tip of the vector
   * @param normalizationBasis The basis on which the length of the difference-vector should be normalized.
   * @return A Point, of which the x and y members represent the components of the normalized vector.
   */
  protected Point normalizeDelta(Point p1, Point p2, float normalizationBasis) {
    float diffX = p2.x - p1.x;
    float diffY = p2.y - p1.y;
    float magnitude = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

    if (magnitude != 0.0f) {
      var diffXNorm = diffX / magnitude * normalizationBasis;
      var diffYNorm = diffY / magnitude * normalizationBasis;
      return new Point(diffXNorm, diffYNorm);
    } else {
      return new Point(diffX, diffY);
    }
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    AnimationState animationState = AnimationState.IDLE;
    switch (movementState) {
      case CAN_MOVE:
        var normalizedDelta = calculateMovementDelta();
        var newPosition = new Point(position.x + normalizedDelta.x, this.position.y + normalizedDelta.y);

        if (level.isTileAccessible(newPosition)) {
          this.position.x += normalizedDelta.x;
          this.position.y += normalizedDelta.y;
        }

        // is the actor moving?
        if (Math.abs(normalizedDelta.x) > 0.0f ||
                Math.abs(normalizedDelta.y) > 0.0f) {
          animationState = AnimationState.RUN;
        }

        if(normalizedDelta.x<0){
          lookLeft=true;
        }
        else if(normalizedDelta.x>0){
          lookLeft=false;
        }
        attackTargetIfReachable(this.position, level, game.getAllEntities());
        break;
      case IS_KNOCKED_BACK:
        this.position = calculateKnockBackTarget();
        animationState = AnimationState.KNOCK_BACK;
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

  private Point calculateMovementDelta() {
    Point newPosition = readMovementInput();
    // calculate normalized delta of this.position and the calculated
    // new position to avoid increased diagonal movement speed
    return normalizeDelta(this.position, newPosition, movementSpeed);
  }

  protected abstract Point readMovementInput();
}