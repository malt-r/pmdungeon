package monsters;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.ICombatable;
import java.util.Timer;
import java.util.TimerTask;
import monsters.strategies.combat.CombatStrategy;
import monsters.strategies.combat.MeleeStrategy;
import monsters.strategies.movement.MovementStrategy;
import monsters.strategies.movement.RandomMovementStrategy;
import stats.Attribute;



/**
 * The base class for any monster.
 *
 * <p>Contains all animations, the current position in the DungeonWorld and movement logic.
 */
public abstract class Monster extends Actor {
  private final Timer respawnTimer;
  private final long respawnDelay;
  protected MovementStrategy currentMovementStrategy;
  protected CombatStrategy currentCombatStrategy;

  /**
   * Constructor of the Monster class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Monster() {
    this.currentMovementStrategy = new RandomMovementStrategy();
    this.currentCombatStrategy = new MeleeStrategy();
    this.respawnTimer = new Timer();
    this.respawnDelay = 500;

    this.stats.addInPlace(Attribute.AttributeType.HEALTH, 100.f);
    this.stats.addInPlace(Attribute.AttributeType.MAX_HEALTH, 100.f);
    this.stats.addInPlace(Attribute.AttributeType.HIT_CHANCE, 0.6f);
    this.stats.addInPlace(Attribute.AttributeType.PHYSICAL_ATTACK_DAMAGE, 50.f);
    this.stats.addInPlace(Attribute.AttributeType.EVASION_CHANCE, 0.15f);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    super.update();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean inRangeFunc(Point p) {
    return this.currentCombatStrategy.rangeFunction(this.getPosition(), p);
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

  /** Generates random Movement Inputs for natural moving of the monster. */
  @Override
  protected Point readMovementInput() {
    return this.currentMovementStrategy.move(getPosition(), level);
  }

  /**
   * Manages damage given by other actors. Also starts a timer for respawning, when the monster has
   * been slain.
   *
   * @param damage The damage value that should be deducted from health.
   */
  @Override
  public void dealDamage(float damage, ICombatable attacker) {
    super.dealDamage(damage, attacker);
    var maxHealth = this.stats.getValue(Attribute.AttributeType.MAX_HEALTH);
    if (this.isDead()) {
      TimerTask respawnTask =
          new TimerTask() {
            @Override
            public void run() {
              findRandomPosition();
              stats.clearModifiers();
            }
          };
      respawnTimer.schedule(respawnTask, respawnDelay);
    }
  }
}
