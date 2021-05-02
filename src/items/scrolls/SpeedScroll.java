package items.scrolls;

import items.IItemVisitor;
import main.Game;

public class SpeedScroll extends Scroll {
  public int healValue=100;

  @Override
  public String getName() {
    return "Healscroll";
  }

  @Override
  protected String getDescription() {
    return "A scroll that heals";
  }

  public SpeedScroll(Game game){
    super(game);
    String[] idleLeftFrames = new String[]{
            "tileset/items/speedscroll.png"

    };
    currentAnimation = createAnimation(idleLeftFrames, 9001);
  }

  @Override
  public void update(){
    drawWithScaling(0.75f,0.75f);
  }

  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
