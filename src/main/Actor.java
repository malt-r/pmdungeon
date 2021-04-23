package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
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
  static Logger l = Logger.getLogger(Actor.class.getName());
  protected Point position;
  protected DungeonWorld level;

  protected Animation idleAnimationRight;
  protected Animation idleAnimationLeft;
  protected Animation runAnimationLeft;
  protected Animation runAnimationRight;
  protected Animation currentAnimation;

  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;

  // implementation of ICombatable -----------------------------------------------------------------------------------
  private final Timer attackTimer;
  protected long attackDelay = 1000;
  protected boolean canAttack;
  protected float movementSpeed = 0.1f;
  // cache a reference to the game to be able to scan all entities for possible attack targets
  private final Game game;
  private ICombatable target;

  // combat-characteristics:
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
  public boolean attack(ICombatable other) {
    boolean success = ICombatable.super.attack(other);
    if (success) {
      l.info("Hit " + other.toString());
    } else {
      l.info("Missed " + other.toString());
    }

    if (other.isDead()) {
      l.info("Other has been slain!");
      // here would the hero gain experience...
    }

    // delay next attack by attackDelay ms
    this.canAttack = false;
    TimerTask resetCanAttackTask = new TimerTask() {
      @Override
      public void run() {
        canAttack = true;
      }
    };
    attackTimer.schedule(resetCanAttackTask, attackDelay);
    return success;
  }

  @Override
  public boolean canAttack() {
    return canAttack;
  }

  @Override
  public void dealDamage(float damage) {
    ICombatable.super.dealDamage(damage);

    if (isDead()) {
      l.info("GAME OVER");
    }
  }

  @Override
  public void heal(float amount) {
    this.health += amount;
    if (this.health > maxHealth) {
      this.health = maxHealth;
    }
  }

  private enum AnimationState {
    IDLE,
    RUN,
  }

  // end of implementation of ICombatable ----------------------------------------------------------------------------

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
  private Point normalizeDelta(Point p1, Point p2, float normalizationBasis) {
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
    // initialize temporary point with current position
    var normalizedDelta = calculateMovementDelta();
    AnimationState animationState = AnimationState.IDLE;
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
    setCurrentAnimation(animationState);
    this.draw();
  }

  /**
   * Override IEntity.deletable and return false for the hero.
   *
   * @return false
   */
  @Override
  public boolean deleteable() {
    return false;
  }

  protected void resetCombatStats() {
    l.info("Actor: resetting combat stats");
    this.setHealth(maxHealth);
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
