package main;

import static stats.Attribute.AttributeType;
import static util.math.Convenience.scaleDelta;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiFunction;
import java.util.function.Function;
import progress.effect.OneShotEffect;
import progress.effect.PersistentEffect;
import stats.Stats;
import util.math.Vec;


/**
 * The controllable player character.
 *
 * <p>Contains all animations, the current position in the DungeonWorld and movement logic.
 */
public abstract class Actor extends DrawableEntity implements Combatable {
  private final Timer attackTimer;
  private final Timer hitTimer;
  /** Animation types. */
  protected Animation idleAnimationRight;

  protected Animation idleAnimationLeft;
  protected Animation runAnimationLeft;
  protected Animation runAnimationRight;
  protected Animation hitAnimationLeft;
  protected Animation hitAnimationRight;
  protected AnimationState animationState = AnimationState.IDLE;
  /**
   * The current MovementState of the actor.
   *
   * @see MovementState
   */
  protected MovementState movementState;
  /** Defines whether the actor can be knocked back or not. */
  protected boolean knockBackAble = false;
  /** The target point for the current knock back. */
  protected Point knockBackTargetPoint;
  /**
   * The speed for being knocked back. Gets added to the position of the actor every update, if it
   * is being knocked back.
   */
  protected float knockBackSpeed = 0.3f;
  /** The distance the actor should be knocked back. */
  protected float knockBackDistance = 0.8f;

  protected long attackDelay = 1000;
  protected boolean canAttack;
  protected Stats stats = new Stats();
  /** The persistent effects, which are currently applied to this actor. */
  protected ArrayList<PersistentEffect> persistentEffects;

  // implementation of ICombatable
  // -----------------------------------------------------------------------------------
  /**
   * The persistent effects, which are currently applied to this actor but are scheduled for
   * removal.
   */
  protected ArrayList<PersistentEffect> effectsScheduledForRemoval;

  private Combatable target;
  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;
  /**
   * Constructor of the Actor class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */

  public Actor() {
    lookLeft = false;
    canAttack = true;
    attackTimer = new Timer();
    hitTimer = new Timer();
    movementState = MovementState.CAN_MOVE;
    this.persistentEffects = new ArrayList<>();
    this.effectsScheduledForRemoval = new ArrayList<>();
    this.generateAnimations();

    this.stats.addInPlace(AttributeType.MOVEMENT_SPEED, 0.1f);
    this.stats.addInPlace(AttributeType.HEALTH, 100.f);
    this.stats.addInPlace(AttributeType.MAX_HEALTH, 100.f);
    this.stats.addInPlace(AttributeType.HIT_CHANCE, 0.7f);
    this.stats.addInPlace(AttributeType.PHYSICAL_ATTACK_DAMAGE, 50.f);
    this.stats.addInPlace(AttributeType.EVASION_CHANCE, 0.15f);
  }

  /**
   * Gets the value of knockBackAble.
   *
   * @return The value of knockBackAble.
   */
  protected boolean isKnockBackAble() {
    return knockBackAble;
  }

  public Stats getStats() {
    return stats;
  }

  /**
   * Gets the health of the actor.
   *
   * @return health of the actor
   */
  @Override
  public float getHealth() {
    return this.stats.getValue(AttributeType.HEALTH);
  }

  /**
   * sets the health of the actor.
   *
   * @param health The new value for the health backing field.
   */
  @Override
  public void setHealth(float health) {
    this.stats.getAttribute(AttributeType.HEALTH).setEffectiveValue(health);
  }

  /**
   * If the actor is passive.
   *
   * @return if the actor is passive
   */
  @Override
  public boolean isPassive() {
    return false;
  }

  /**
   * if the actor has a target for combat.
   *
   * @return if the actor has a target
   */
  @Override
  public boolean hasTarget() {
    return this.target != null;
  }

  /**
   * Returns of the actor has a target.
   *
   * @return if the acor ahs a target
   */
  @Override
  public Combatable getTarget() {
    return this.target;
  }

  /**
   * Sets the target of the actor.
   *
   * @param target The ICombatable to cache as a target.
   */
  @Override
  public void setTarget(Combatable target) {
    this.target = target;
  }

  /**
   * Returns of the other is friendly for combat.
   *
   * @param other The other ICombatable.
   * @return if the other is friendly
   */
  @Override
  public boolean isOtherFriendly(Combatable other) {
    return false;
  }

  /**
   * Gets the Hitchance for combat.
   *
   * @return value of the hitchance
   */
  @Override
  public float getHitChance() {
    return this.stats.getValue(AttributeType.HIT_CHANCE);
  }

  /**
   * Gets the evasion rate of the actor.
   *
   * @return evasion rate of the actor
   */
  @Override
  public float getEvasionChance() {
    return this.stats.getValue(AttributeType.EVASION_CHANCE);
  }

  /**
   * gets the damage value for combat.
   *
   * @return damage value of the actor
   */
  @Override
  public float getDamage() {
    return this.stats.getValue(AttributeType.PHYSICAL_ATTACK_DAMAGE);
  }

  protected boolean inRangeFunc(Point p) {
    return new Vec(this.getPosition()).subtract(new Vec(p)).magnitude() < 1.0f;
  }

  /**
   * Attack implementation for combat.
   *
   * @param other The ICombatable to attack.
   * @return if the attack was sucessfull
   */
  @Override
  public float attack(Combatable other) {
    float damage = Combatable.super.attack(other);
    // delay next attack by attackDelay ms
    this.canAttack = false;
    TimerTask resetCanAttackTask =
        new TimerTask() {
          @Override
          public void run() {
            canAttack = true;
          }
        };
    attackTimer.schedule(resetCanAttackTask, attackDelay);
    return damage;
  }

  /**
   * Resolves if another attack is possible.
   *
   * @return an attack is possible
   */
  @Override
  public boolean canAttack() {
    return Combatable.super.canAttack() && canAttack;
  }

  /**
   * Deals damage to this actor.
   *
   * @param damage The amount to decrease the health by.
   * @param attacker The ICombatable which deals the damage.
   */
  @Override
  public void dealDamage(float damage, Combatable attacker) {
    Combatable.super.dealDamage(damage, attacker);
    this.movementState = MovementState.HIT;
    if (isKnockBackAble() && attacker != null) {
      initiateKnockBack(attacker);
    }
  }

  /**
   * Heals the actor.
   *
   * @param amount The amount to increase the health value by.
   */
  @Override
  public void heal(float amount) {
    var healthAttr = this.stats.getAttribute(AttributeType.HEALTH);
    var maxHealthAttr = this.stats.getAttribute(AttributeType.MAX_HEALTH);
    var val = healthAttr.getEffectiveValue();

    if (val + amount > maxHealthAttr.getEffectiveValue()) {
      healthAttr.setEffectiveValue(maxHealthAttr.getEffectiveValue());
    } else {
      healthAttr.setEffectiveValue(healthAttr.getEffectiveValue() + amount);
    }
  }

  /** Set this.lookLeft according to position of target. */
  protected void lookAtTarget() {
    if (this.target instanceof IDrawable) {
      Point targetPosition = ((IDrawable) this.target).getPosition();
      if (this.getPosition().x > targetPosition.x) {
        lookLeft = true;
      } else if (this.getPosition().x < targetPosition.x) {
        lookLeft = false;
      }
    }
  }
  // end ICombatable implementation ----------------------------------------------------------------

  // Abilitysystem implementation ------------------------------------------------------------------

  /**
   * Call the update-method of all stored persistent effects. Remove any persistent effects, which
   * are scheduled for removal.
   */
  protected void updatePersistentEffects() {
    for (PersistentEffect effect : persistentEffects) {
      effect.update(this);
    }

    for (PersistentEffect effect : effectsScheduledForRemoval) {
      mainLogger.info("Removing persistent effect" + effect);
      effect.onRemoval(this);
      persistentEffects.remove(effect);
    }
    effectsScheduledForRemoval.clear();
  }

  /**
   * Schedule persistent effect for removal from this actor. Will be removed in
   * updatePersistentEffects
   *
   * @param effect The effect to remove.
   */
  public void scheduleForRemoval(PersistentEffect effect) {
    mainLogger.info("Scheduling effect for removal: " + effect.toString());
    if (!effectsScheduledForRemoval.contains(effect)) {
      effectsScheduledForRemoval.add(effect);
    }
  }

  /**
   * Apply a one-shot effect to this actor.
   *
   * @param effect The effect to apply.
   */
  public void applyOneShotEffect(OneShotEffect effect) {
    effect.applyTo(this);
  }

  /**
   * Apply persistent effect to this actor. Passed effect will be stored in this.persistentEffects
   * and updated on update of the Actor.
   *
   * @param effect The effect to apply.
   */
  public void applyPersistentEffect(PersistentEffect effect) {
    mainLogger.info("Applying persistent effect: " + effect.toString());
    effect.onApply(this);
    if (!persistentEffects.contains(effect)) {
      persistentEffects.add(effect);
    }
  }

  /**
   * Starts a knock back and calculates the knockBackTargetPoint.
   *
   * @param other The attacker, which caused the knock back. Used to calculate the destination in
   *     which the knock back should be performed (the opposite of the difference vector of
   *     this.position and other.position). Should implement IDrawable.
   */
  protected void initiateKnockBack(Combatable other) {
    if (other instanceof IDrawable) {
      var attackerPosition = ((IDrawable) other).getPosition();
      initiateKnockBackFromPoint(attackerPosition, knockBackDistance);
    }
  }

  /**
   * Starts a knock and calculates the knockBackTargetPoint.
   *
   * @param point The point, from which the knock back is initiated. Used to calculate the
   *     destination in which the knock back should be performed.
   */
  // TODO: this should use a falloff function so that the knockback-distance gets smaller dependent
  // of the distance from the point
  public void initiateKnockBackFromPoint(Point point, float knockBackDistance) {
    var diff = scaleDelta(this.getPosition(), point, knockBackDistance);
    this.knockBackTargetPoint = (new Vec(this.getPosition())).subtract(diff).toPoint();
    this.movementState = MovementState.IS_KNOCKED_BACK;
  }

  // end Abilitysystem implementation --------------------------------------------------------------

  /**
   * Calculate the target point for the knock back in this frame (should be called in update). Ends
   * the knock back, if the magnitude of the difference vector is less than the knockBackSpeed
   * (otherwise the actor may overshoot the knockBackTargetPoint, which can lead to oscillation
   * around the knockBackTargetPoint). Ends the knock back, if the calculated target point lies in a
   * tile, which is not accessible.
   *
   * @return The target point for a knock back in the current frame.
   */
  protected Point calculateKnockBackTarget() {
    var positionVec = new Vec(this.getPosition());
    var knockBackTargetVec = new Vec(this.knockBackTargetPoint);

    var diffMagnitude = positionVec.subtract(knockBackTargetVec).magnitude();
    if (diffMagnitude < knockBackSpeed) {
      movementState = MovementState.CAN_MOVE;
      return this.getPosition();
    } else {
      var normalizedDiff =
          scaleDelta(this.getPosition(), this.knockBackTargetPoint, knockBackSpeed);
      var targetPoint = positionVec.add(normalizedDiff).toPoint();

      if (!level.isTileAccessible(targetPoint)) {
        movementState = MovementState.CAN_MOVE;
        return this.getPosition();
      } else {
        return targetPoint;
      }
    }
  }

  /** Generates the animations of the actor. */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/default/default_anim.png",
        };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames =
        new String[] {
          "tileset/default/default_anim.png",
        };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames =
        new String[] {
          "tileset/default/default_anim.png",
        };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames =
        new String[] {
          "tileset/default/default_anim.png",
        };
    runAnimationRight = createAnimation(runRightFrames, 4);

    String[] hitLeftFrames = new String[] {"tileset/default/default_anim.png"};
    hitAnimationLeft = createAnimation(hitLeftFrames, 1);

    String[] hitRightFrames = new String[] {"tileset/default/default_anim.png"};
    hitAnimationRight = createAnimation(hitRightFrames, 1);
  }

  /**
   * Sets the current animation.
   *
   * @param animationState animation state which the actor is currently in
   */
  private void setCurrentAnimation(AnimationState animationState) {
    switch (animationState) {
      case RUN:
        this.currentAnimation = lookLeft ? this.runAnimationLeft : this.runAnimationRight;
        break;
      case KNOCK_BACK:
      case HIT:
        this.currentAnimation = lookLeft ? this.hitAnimationLeft : this.hitAnimationRight;
        break;
      case IDLE:
      default:
        this.currentAnimation = lookLeft ? this.idleAnimationLeft : this.idleAnimationRight;
    }
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    animationState = AnimationState.IDLE;

    updatePersistentEffects();

    switch (movementState) {
      case CAN_MOVE:
        var movementDelta = calculateMovementDelta();
        var newPosition = (movementDelta.add(new Vec(this.getPosition()))).toPoint();

        if (level.isTileAccessible(newPosition)) {
          this.setPosition(newPosition);

          // is the actor moving?
          if (movementDelta.magnitude() > 0.0f) {
            animationState = AnimationState.RUN;
          }
        }

        // set look direction
        if (hasTarget()) {
          lookAtTarget();
        } else if (movementDelta.getX() < 0) {
          lookLeft = true;
        } else if (movementDelta.getX() > 0) {
          lookLeft = false;
        }

        BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder =
            (p1, p2) -> Game.getInstance().getEntitiesInCoordRange(p1, p2);
        Function<Point, Boolean> inRange = (p) -> inRangeFunc(p);

        attackTargetIfReachable(this.getPosition(), inRange, entityFinder);

        if (readPickupInput()) {
          handleItemPicking();
        }
        break;
      case IS_KNOCKED_BACK:
        this.setPosition(calculateKnockBackTarget());
        animationState = AnimationState.KNOCK_BACK;
        break;
      case HIT:
        animationState = AnimationState.HIT;
        TimerTask resetMovementState =
            new TimerTask() {
              @Override
              public void run() {
                movementState = MovementState.CAN_MOVE;
              }
            };
        hitTimer.schedule(resetMovementState, 300);
        break;
      case SUSPENDED:
        break;
      default:
        break;
    }
    setCurrentAnimation(animationState);
    this.draw();
  }

  /** Rests tje combat stats of the actor. E.g. after the actor died. */
  protected void resetStats() {
    this.stats.clearModifiers();
    this.movementState = MovementState.CAN_MOVE;
    this.canAttack = true;
  }

  /** Set reference to DungeonWorld and spawn player at random position in the level. */
  @Override
  public void setLevel(DungeonWorld level) {
    super.setLevel(level);
    this.resetStats();
  }

  private Vec calculateMovementDelta() {
    Point newPosition = readMovementInput();
    // calculate normalized delta of this.position and the calculated
    // new position to avoid increased diagonal movement speed
    return scaleDelta(
        this.getPosition(), newPosition, this.stats.getValue(AttributeType.MOVEMENT_SPEED));
  }

  /**
   * abstract method which has to be overwritten in the sub classes this makes it possible to
   * control an actor autoamticly or via key presisng.
   *
   * @return returns the new point where the actor should be moved
   */
  protected abstract Point readMovementInput();

  /**
   * Has to be overwritten for the hero, monsters do this automaticly.
   *
   * @return if pickup input has been read
   */
  protected boolean readPickupInput() {
    return false;
  }

  /** Handles Item picking of the actor. */
  protected void handleItemPicking() {}

  /**
   * MovementState switches between different movement-characteristics of the actor. CAN_MOVE: the
   * actor can move normally. IS_KNOCKED_BACK: the actor is currently being knocked back and can not
   * be moved by input.
   */
  protected enum MovementState {
    CAN_MOVE,
    IS_KNOCKED_BACK,
    HIT,
    SUSPENDED
  }

  private enum AnimationState {
    IDLE,
    RUN,
    KNOCK_BACK,
    HIT
  }
}
