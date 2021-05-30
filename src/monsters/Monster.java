package monsters;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.ICombatable;
import monsters.strategies.combat.CombatStrategy;
import monsters.strategies.combat.MeleeStrategy;
import monsters.strategies.movement.MovementStrategy;
import monsters.strategies.movement.RandomMovementStrategy;
import java.util.*;

/**
 * The base class for any monster.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public abstract class Monster extends Actor {
  private final Timer respawnTimer;
  private final long respawnDelay;
  protected MovementStrategy currentMovementStrategy;
  protected CombatStrategy currentCombatStrategy;

  /**
   * Constructor of the Monster class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Monster() {
    this.currentMovementStrategy = new RandomMovementStrategy();
    this.currentCombatStrategy = new MeleeStrategy();
    this.respawnTimer = new Timer();
    this.respawnDelay = 500;

    // combat-characteristics:
    health = 100.f;
    maxHealth = 100.f;

    baseHitChance = 0.6f;
    hitChanceModifierWeapon= 1.f;
    hitChanceModifierScroll= 1.f;

    baseAttackDamage = 50;
    attackDamageModifierWeapon = 1.f;
    attackDamageModifierScroll = 1.f;

    baseEvasionChance = 0.15f;
    evasionChanceModifierWeapon = 1.f;
    evasionChanceModifierScroll = 1.f;
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    super.update();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean inRangeFunc(Point p){
    return this.currentCombatStrategy.RangeFunction(this.getPosition(),p);
  }

  /**
   * Monster shouldn't attack other monsters.
   *
   * @param other other combatable instance
   * @return if the other combatable instance is friendly
   */
  @Override
  public boolean isOtherFriendly(ICombatable other) {
    return other instanceof Monster;
  }

  /**
   * Generates random Movement Inputs for natural moving of the monster
   */
  @Override
  protected Point readMovementInput(){
    //if(hasTarget()){return new Point(this.getPosition());}
    return this.currentMovementStrategy.Move(getPosition(),level);
  }

  /**
   * Manages damage given by other actors. Also starts a timer for respawning, when the monster has been slain.
   *
   * @param damage  The damage value that should be deducted from health.
   */
  @Override
  public void dealDamage(float damage, ICombatable attacker) {
    super.dealDamage(damage, attacker);
    if (this.isDead()) {
      TimerTask respawnTask = new TimerTask() {
        @Override
        public void run() {
          findRandomPosition();
          setHealth(maxHealth);
        }
      };
      respawnTimer.schedule(respawnTask, respawnDelay);
    }
  }
}