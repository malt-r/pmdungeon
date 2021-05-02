package items.shields;

import items.IItemVisitor;
import main.Game;

public class WoodShield extends Shield{
  public WoodShield(Game game) {
    super(game);
    this.defenseValue= 1.1f;
    String[] idleLeftFrames = new String[]{
            "tileset/items/shield_wood.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  @Override
  public String getName() {
    return "Wooden Shield";
  }

  @Override
  protected String getDescription() {
    return "A simple shield. Better than a sheet of paper.";
  }

  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
