package monsters;

import main.Game;
import monsters.strategies.movement.FollowHeroStrategy;
import monsters.strategies.movement.MovementStrategy;
import monsters.strategies.movement.RandomMovementStrategy;
import util.math.Convenience;

/**
 * Lizard Monster.
 *
 * <p>Contains all animations, the current position in the DungeonWorld and movement logic.
 */
public class LizardMonster extends Monster {
  private final MovementStrategy followHeroStrategy;
  private final MovementStrategy randomMovementStrategy;

  /**
   * Constructor of the Lizardmonster class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public LizardMonster() {
    super();
    this.followHeroStrategy = new FollowHeroStrategy();
    this.randomMovementStrategy = new RandomMovementStrategy();
  }

  /** Generates the run and idle animation for the Lizard monster. */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/lizard/lizard_m_idle_left_anim_f0.png",
          "tileset/lizard/lizard_m_idle_left_anim_f1.png",
          "tileset/lizard/lizard_m_idle_left_anim_f2.png",
          "tileset/lizard/lizard_m_idle_left_anim_f3.png"
        };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames =
        new String[] {
          "tileset/lizard/lizard_m_idle_anim_f0.png",
          "tileset/lizard/lizard_m_idle_anim_f1.png",
          "tileset/lizard/lizard_m_idle_anim_f2.png",
          "tileset/lizard/lizard_m_idle_anim_f3.png"
        };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames =
        new String[] {
          "tileset/lizard/lizard_m_run_left_anim_f0.png",
          "tileset/lizard/lizard_m_run_left_anim_f1.png",
          "tileset/lizard/lizard_m_run_left_anim_f2.png",
          "tileset/lizard/lizard_m_run_left_anim_f3.png"
        };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames =
        new String[] {
          "tileset/lizard/lizard_m_run_anim_f0.png",
          "tileset/lizard/lizard_m_run_anim_f1.png",
          "tileset/lizard/lizard_m_run_anim_f2.png",
          "tileset/lizard/lizard_m_run_anim_f3.png"
        };
    runAnimationRight = createAnimation(runRightFrames, 4);

    String[] hitLeftFrames = new String[] {"tileset/lizard/lizard_m_idle_left_anim_f0_hit.png"};
    hitAnimationLeft = createAnimation(hitLeftFrames, 1);

    String[] hitRightFrames = new String[] {"tileset/lizard/lizard_m_idle_anim_f0_hit.png"};
    hitAnimationRight = createAnimation(hitRightFrames, 1);
  }

  /** {@inheritDoc} */
  @Override
  public void update() {
    super.update();
    var startTile = Convenience.convertPointToTile(getPosition(), level);
    var endTile = Convenience.convertPointToTile(Game.getInstance().getHero().getPosition(), level);
    var pathToHero = level.findPath(startTile, endTile);
    if (pathToHero.getCount() < 5) {
      currentMovementStrategy = followHeroStrategy;
    } else {
      currentMovementStrategy = this.randomMovementStrategy;
    }
  }
}
