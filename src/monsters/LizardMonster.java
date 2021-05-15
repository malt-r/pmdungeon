package monsters;

/**
 * Lizard Monster.
 * <p>
 *   Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class LizardMonster  extends Monster {
  /**
   * Constructor of the Lizardmonster class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public LizardMonster() {
    super();
  }
  /**
   * Generates the run and idle animation for the Lizard monster.
   */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/lizard/lizard_m_idle_left_anim_f0.png",
            "tileset/lizard/lizard_m_idle_left_anim_f1.png",
            "tileset/lizard/lizard_m_idle_left_anim_f2.png",
            "tileset/lizard/lizard_m_idle_left_anim_f3.png"
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[]{
            "tileset/lizard/lizard_m_idle_anim_f0.png",
            "tileset/lizard/lizard_m_idle_anim_f1.png",
            "tileset/lizard/lizard_m_idle_anim_f2.png",
            "tileset/lizard/lizard_m_idle_anim_f3.png"
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[]{
            "tileset/lizard/lizard_m_run_left_anim_f0.png",
            "tileset/lizard/lizard_m_run_left_anim_f1.png",
            "tileset/lizard/lizard_m_run_left_anim_f2.png",
            "tileset/lizard/lizard_m_run_left_anim_f3.png"
    };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames = new String[]{
            "tileset/lizard/lizard_m_run_anim_f0.png",
            "tileset/lizard/lizard_m_run_anim_f1.png",
            "tileset/lizard/lizard_m_run_anim_f2.png",
            "tileset/lizard/lizard_m_run_anim_f3.png"
    };
    runAnimationRight = createAnimation(runRightFrames, 4);

    String[] hitLeftFrames = new String[]{
            "tileset/lizard/lizard_m_idle_left_anim_f0_hit.png"
    };
    hitAnimationLeft = createAnimation(hitLeftFrames, 1);

    String[] hitRightFrames = new String[]{
            "tileset/lizard/lizard_m_idle_anim_f0_hit.png"
    };
    hitAnimationRight = createAnimation(hitRightFrames, 1);
  }
}