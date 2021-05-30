package items.weapons;

import items.EquipableItem;

/**
 * Weapon base class.
 *
 * <p>Contains everything that describes a shield.
 */
public abstract class Weapon extends EquipableItem {
  protected float range;
  protected int condition;

  /**
   * Constructor of the Weapon base class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Weapon() {
    super();
  }

  public float getRange() {
    return range;
  }

  /**
   * Reduces the condition of the weapon after a sucessfull hit
   *
   * @param reduce reduceamount which should be deducted from the weapon
   * @return returns if the weapon broke
   */
  public boolean reduceCondition(int reduce) {
    condition -= reduce;
    return condition > 0;
  }

  /**
   * Returns the condition value of the shield which can be used for display purposes
   *
   * @return condition of the weapon
   */
  public float getCondition() {
    return condition;
  }
}
