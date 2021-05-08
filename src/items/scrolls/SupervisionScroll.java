package items.scrolls;

import items.IItemVisitor;
import items.potions.Potion;

public class SupervisionScroll extends Scroll {
  public SupervisionScroll(){
    super();
    String[] idleLeftFrames = new String[]{
            "tileset/items/supervisionscroll_2.png"

    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
  @Override
  public String getName() {
    return "Supervision Potion";
  }

  @Override
  protected String getDescription() {
    return "After drinking this potion you can see traps";
  }
  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
