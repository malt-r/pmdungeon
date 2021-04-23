package monsters;

import main.Game;

public class DemonMonster extends Monster{
  public DemonMonster(Game game){
  super(game);
  }
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/default_anim.png"
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[]{
            "tileset/default_anim.png"
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[]{
            "tileset/default_anim.png"
    };
    runAnimationLeft = createAnimation(runLeftFrames, 6);

    String[] runRightFrames = new String[]{
            "tileset/default_anim.png"
    };
    runAnimationRight = createAnimation(runRightFrames, 6);
  }
}