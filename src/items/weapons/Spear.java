package items.weapons;

import items.IItemVisitor;

public class Spear extends Weapon{
  public Spear() {
    super();
    this.attackDamageModifier=1.5f;
    this.hitChanceModifier=0.7f;
    this.condition=100;

    String[] idleLeftFrames = new String[]{
            "tileset/items/weapon_spear.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  @Override
  public String getName() {
    return "Spear";
  }

  @Override
  protected String getDescription() {
    return "Pointy Spear";
  }

  @Override
  public void update(){
    drawWithScaling(0.25f,1.0f);
  }
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
