package monsters;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import main.Game;
import monsters.strategies.combat.CombatStrategy;
import monsters.strategies.combat.MeleeStrategy;
import monsters.strategies.movement.FollowHeroStrategy;
import monsters.strategies.movement.MovementStrategy;
import monsters.strategies.movement.NoMovementStrategy;
import monsters.strategies.movement.RandomMovementStrategy;
import util.math.Convenience;

/**
 * Chest Monster.
 *
 * <p>Contains all animations, the current position in the DungeonWorld and movement logic.
 */
public class ChestMonster extends Monster {
  private final MovementStrategy noMovementStrategy;
  private final MovementStrategy randomMovementStrategy;
  private final MovementStrategy followHeroStrategy;
  private final CombatStrategy meleeCombatStrategy;
  private boolean awake = false;

  private Animation sleepAnimation;

  /**
   * Constructor of the ChestMonster class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public ChestMonster() {
    super();
    this.noMovementStrategy = new NoMovementStrategy();
    this.randomMovementStrategy = new RandomMovementStrategy();
    this.followHeroStrategy = new FollowHeroStrategy();
    this.currentMovementStrategy = this.noMovementStrategy;
    this.meleeCombatStrategy = new MeleeStrategy();
    this.currentCombatStrategy = this.meleeCombatStrategy;
  }

  /** Generates the run and idle animation for the ChestMonster. */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[] {"tileset/chestmonster/chest_mimic_open_anim_f0.png"};
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[] {"tileset/chestmonster/chest_mimic_open_anim_f0.png"};
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames =
        new String[] {
          "tileset/chestmonster/chest_mimic_open_anim_f0.png",
          "tileset/chestmonster/chest_mimic_open_anim_f1.png",
          "tileset/chestmonster/chest_mimic_open_anim_f2.png"
        };
    runAnimationLeft = createAnimation(runLeftFrames, 6);

    String[] runRightFrames =
        new String[] {
          "tileset/chestmonster/chest_mimic_open_anim_f0.png",
          "tileset/chestmonster/chest_mimic_open_anim_f1.png",
          "tileset/chestmonster/chest_mimic_open_anim_f2.png"
        };
    runAnimationRight = createAnimation(runRightFrames, 6);

    String[] hitLeftFrames = new String[] {"tileset/chestmonster/chest_mimic_open_anim_f0.png"};
    hitAnimationLeft = createAnimation(hitLeftFrames, 6);

    String[] hitRightFrames = new String[] {"tileset/chestmonster/chest_mimic_open_anim_f0.png"};
    hitAnimationRight = createAnimation(hitRightFrames, 6);

    String[] sleepFrames = new String[] {"tileset/chestmonster/chest_mimic_open_anim_f0.png"};
    sleepAnimation = createAnimation(sleepFrames, Integer.MAX_VALUE);
  }

  /** Runs every frame and manages the switch between different strategies. */
  @Override
  public void update() {
    var startTile = Convenience.convertPointToTile(getPosition(), level);
    var endTile = Convenience.convertPointToTile(Game.getInstance().getHero().getPosition(), level);
    var pathToHero = level.findPath(startTile, endTile);

    if (pathToHero.getCount() < 3) {
      awake = true;
      idleAnimationLeft = runAnimationLeft;
      idleAnimationRight = runAnimationRight;
    }

    if (awake) {
      if (pathToHero.getCount() < 5) {
        currentMovementStrategy = followHeroStrategy;
      } else {
        currentMovementStrategy = randomMovementStrategy;
      }
    }
    if (!awake) {
      currentAnimation = sleepAnimation;
    }
    super.update();
  }
}
