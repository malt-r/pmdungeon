package items.potions;

import items.IItemVisitor;
import main.Game;

public class PoisonPotion extends Potion {
  public int damageValue = 100;

  @Override
  public String getName() {
    return "Poison Potion";
  }

  @Override
  protected String getDescription() {
    return "I'm a poisonly potion";
  }

  public PoisonPotion(Game game) {
    super(game);
    String[] idleLeftFrames = new String[]{
            "tileset/items/flask_big_green.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  @Override
  public void update() {
    drawWithScaling(1.0f, 1.0f);
  }
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
