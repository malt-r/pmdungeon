package items.weapons;

import items.EquipableItem;
/**
 * Weapon base class.
 * <p>
 *   Contains everything that describes a shield.
 * </p>
 */
public abstract class Weapon extends EquipableItem {
  protected float attackDamageModifier;
  /**
   *  Returns the attackDamageModifier value of the shield which can be used for display purposes
   *  @return attackDamageModifier of the shield
   */
  public float getAttackDamageModifier(){return attackDamageModifier;};
  protected float hitChanceModifier;
  /**
   *  Returns the hitChanceModifier value of the shield which can be used for display purposes
   *  @return hitChanceModifier of the shield
   */
  public float getHitChanceModifier(){return hitChanceModifier;};
  protected int condition;
  public boolean reduceCondition(int reduce) {
    condition -= reduce;
    if (condition <= 0 ){
      return false;
    }
    return true;
  }
  /**
   *  Returns the condition value of the shield which can be used for display purposes
   *  @return condition of the weapon
   */
  public float getCondition(){return condition;};
  /**
   * Constructor of the Weapon base class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Weapon() {
    super();
  }
}
