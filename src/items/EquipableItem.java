package items;

import stats.Modifier;

import java.util.ArrayList;

/**
 * Abstract Base class for equioable items.
 * <p>
 *   Contains everything that describes an equipable item.
 * </p>
 */
public abstract class EquipableItem extends Item{

  protected ArrayList<Modifier> modifiers = new ArrayList<>();

  public ArrayList<Modifier> getModifiers() {
    return modifiers;
  }

  /**
   * Constructor of the EquipableItem class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public EquipableItem() {
    super();
  }

  /**
   * Defines if an item in the inventory ist stackable
   * @return Returns if an item is stackable
   */
}
