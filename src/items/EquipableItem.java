package items;

import stats.Modifier;

import java.util.ArrayList;

/**
 * Abstract Base class for equioable items.
 *
 * <p>Contains everything that describes an equipable item.
 */
public abstract class EquipableItem extends Item {

  protected ArrayList<Modifier> modifiers = new ArrayList<>();

  /**
   * Constructor of the EquipableItem class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public EquipableItem() {
    super();
  }

  public ArrayList<Modifier> getModifiers() {
    return modifiers;
  }

  /**
   * Defines if an item in the inventory ist stackable
   *
   * @return Returns if an item is stackable
   */
}
