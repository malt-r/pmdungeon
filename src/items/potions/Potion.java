package items.potions;

import items.UseableItem;

/**
 * Abstract Potion class.
 *
 * <p>Contains everything that describes a generic potion and is used to group all potions.
 */
public abstract class Potion extends UseableItem {
  /**
   * Constructor of the Potion class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Potion() {
    super();
  }

  /** Draws the potion with specific offsets and scalings */
  @Override
  public void update() {
    draw(-0.85F, -0.75F, 0.75f, 0.75f);
  }
}
