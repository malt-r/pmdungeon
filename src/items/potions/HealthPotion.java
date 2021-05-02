package items.potions;

import items.IItemVisitor;
import main.Game;


public class HealthPotion extends Potion {
  public int healValue=100;

  @Override
  public String getName() {
    return "Health Potion";
  }

  @Override
  protected String getDescription() {
    return "A potion that heals";
  }

  public HealthPotion(Game game){
    super(game);
    String[] idleLeftFrames = new String[]{
            "tileset/items/flask_big_red.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, 9001);
  }

  @Override
  public void update(){
    drawWithScaling(1.0f,1.0f);
  }
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }

}
