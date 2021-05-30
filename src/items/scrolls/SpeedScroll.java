package items.scrolls;

import items.IItemVisitor;

/**
 * Speed scroll.
 *
 * <p>Contains everything that describes a Speed Scroll.
 */
public class SpeedScroll extends Scroll {
  protected float speedMultiplier = 1.5f;

  /**
   * Constructor of the Speedscroll class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public SpeedScroll() {
    super();
    String[] idleLeftFrames = new String[] {"tileset/items/speedscroll.png"};

    currentAnimation = createAnimation(idleLeftFrames, 9001);
  }

  /**
   * Returns the speedMultiplier value of the scroll which can be used for display purposes
   *
   * @return attackbonus of the scroll
   */
  public float getSpeedMultiplier() {
    return speedMultiplier;
  }

  /**
   * Returns the name of the scroll which can be used for display purposes
   *
   * @return Name of the scroll
   */
  @Override
  public String getName() {
    return "Speedscroll";
  }

  /**
   * Returns the deescription of the scroll which can be used for display purposes
   *
   * @return description of the scroll
   */
  @Override
  protected String getDescription() {
    return "A scroll that heals";
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
