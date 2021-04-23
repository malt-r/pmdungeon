package monsters;

import main.Game;

public class LizardMonster  extends Monster {
  public LizardMonster(Game game) {
    super(game);
  }

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
  }
}