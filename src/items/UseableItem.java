package items;

/**
 * Abstract UseableItem base class which is the base class for every usaable item
 */
public abstract class UseableItem extends Item{
  /**
   * Constructor of the item class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public UseableItem() {

  }

  /**
   * Returns if the item is stackable
   * @return if the item is stackable
   */
  @Override
  public boolean isStackable() {
    return true;
  }
}
