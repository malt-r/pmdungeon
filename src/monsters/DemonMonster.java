package monsters;

import main.Game;
/**
 * Demon Monster.
 * <p>
 *   Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class DemonMonster extends Monster{
  /**
   * Constructor of the Demonmonster class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   *  @param game Game of the demonmonster
   */
  public DemonMonster(Game game){
  super(game);
  }
  /**
   * Generates the run and idle animation for the Demon monster.
   */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/demon/big_demon_idle_left_anim_f0.png",
            "tileset/demon/big_demon_idle_left_anim_f1.png",
            "tileset/demon/big_demon_idle_left_anim_f2.png",
            "tileset/demon/big_demon_idle_left_anim_f3.png"
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[]{
            "tileset/demon/big_demon_idle_anim_f0.png",
            "tileset/demon/big_demon_idle_anim_f1.png",
            "tileset/demon/big_demon_idle_anim_f2.png",
            "tileset/demon/big_demon_idle_anim_f3.png"
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[]{
            "tileset/demon/big_demon_run_left_anim_f0.png",
            "tileset/demon/big_demon_run_left_anim_f1.png",
            "tileset/demon/big_demon_run_left_anim_f2.png",
            "tileset/demon/big_demon_run_left_anim_f3.png"
    };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames = new String[]{
            "tileset/demon/big_demon_run_anim_f0.png",
            "tileset/demon/big_demon_run_anim_f1.png",
            "tileset/demon/big_demon_run_anim_f2.png",
            "tileset/demon/big_demon_run_anim_f3.png"
    };
    runAnimationRight = createAnimation(runRightFrames, 4);
  }
}