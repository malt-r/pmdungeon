package items.potions;

import items.UseableItem;
/**
 *  Abstract Potion class.
 * <p>
 *   Contains everything that describes a generic potion and is used to group all potions.
 * </p>
 */
public abstract class Potion extends UseableItem {
  /**
   * Constructor of the Potion class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Potion() {
    super();
  }
}
