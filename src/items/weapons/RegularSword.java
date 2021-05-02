package items.weapons;

import items.IItemVisitor;
import main.Game;

public class RegularSword extends Weapon {
  public RegularSword(Game game){
    super(game);
    this.attackDamageModifier=1.2f;
    this.hitChanceModifier=1.2f;
    this.condition=100;
    String[] idleLeftFrames = new String[]{
            "tileset/items/weapon_regular_sword.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);

  }
  @Override
  public String getName() {
    return "Regular Sword";
  }

  @Override
  protected String getDescription() {
    return "Thicc sword";
  }

  @Override
  public void update(){
    drawWithScaling(0.5f,1.0f);
  }
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
