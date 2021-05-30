package items.scrolls;

import items.ItemVisitor;

/**
 * Supervision scroll.
 *
 * <p>Contains everything that describes a Speed Scroll.
 */
public class SupervisionScroll extends Scroll {
  /**
   * Constructor of the Supervision Scroll class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public SupervisionScroll() {
    super();
    String[] idleLeftFrames = new String[] {"tileset/items/supervisionscroll_2.png"};

    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Returns the name of the scroll which can be used for display purposes.
   *
   * @return Name of the scroll
   */
  @Override
  public String getName() {
    return "Supervision Potion";
  }

  /**
   * Returns the description of the potion which can be used for display purposes.
   *
   * @return description of the scroll
   */
  @Override
  protected String getDescription() {
    return "After drinking this potion you can see traps";
  }

  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   *
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(ItemVisitor visitor) {
    visitor.visit(this);
  }
}
