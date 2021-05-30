package items.weapons;

import items.EquipableItem;
import stats.Modifier;

import java.util.ArrayList;

/**
 * Weapon base class.
 * <p>
 *   Contains everything that describes a shield.
 * </p>
 */
public abstract class Weapon extends EquipableItem {
  protected float range;
  public float getRange(){ return range; }


  //protected float attackDamageModifier;
  /**
   *  Returns the attackDamageModifier value of the shield which can be used for display purposes
   *  @return attackDamageModifier of the shield
   */
  //public float getAttackDamageModifier(){return attackDamageModifier;};
  //protected float hitChanceModifier;
  /**
   *  Returns the hitChanceModifier value of the shield which can be used for display purposes
   *  @return hitChanceModifier of the shield
   */
  //public float getHitChanceModifier(){return hitChanceModifier;};
  // TODO: solve this with health-stat
  protected int condition;

  /**
   * Reduces the condition of the weapon after a sucessfull hit
   * @param reduce reduceamount which should be deducted from the weapon
   * @return  returns if the weapon broke
   */
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
