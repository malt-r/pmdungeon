package items.shields;

import items.IItemVisitor;
import main.Game;

public class EagleShield extends Shield{
  public EagleShield(Game game) {
    super(game);
    this.defenseValue=100;
    String[] idleLeftFrames = new String[]{
            "tileset/items/shield_red_yellow_eagle.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  @Override
  public String getName() {
    return "The mighty red Shield";
  }

  @Override
  protected String getDescription() {
    return "Forgotten for centuries, one would say that only one of this kind of shield exists.";
  }
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }

}
