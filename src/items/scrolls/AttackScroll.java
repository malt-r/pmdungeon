package items.scrolls;

import items.IItemVisitor;

/**
 * Attack Scroll.
 *
 * <p>Contains everything that describes a Attack scroll.
 */
public class AttackScroll extends Scroll {
  protected int attackBonus = 5;

  /**
   * Constructor of the Attackscroll class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public AttackScroll() {
    super();
    String[] idleLeftFrames = new String[] {"tileset/items/attackscroll.png"};

    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Returns the heal value of the potion which can be used for display purposes
   *
   * @return attackbonus of the scroll
   */
  public int getAttackBonus() {
    return attackBonus;
  }

  /**
   * Returns the name of the scroll which can be used for display purposes
   *
   * @return Name of the scroll
   */
  @Override
  public String getName() {
    return "Attackscroll";
  }

  /**
   * Returns the deescription of the potion which can be used for display purposes
   *
   * @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "A scroll that increases attack value";
  }

  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   *
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }
}
