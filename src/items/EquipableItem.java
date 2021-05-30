package items;

import java.util.ArrayList;
import stats.Modifier;

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

  /**
   * Gets the modifiers.
   *
   * @return The modifiers.
   */
  public ArrayList<Modifier> getModifiers() {
    return modifiers;
  }
}
