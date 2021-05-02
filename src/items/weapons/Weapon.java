package items.weapons;

import items.EquipableItem;
import main.Game;

public abstract class Weapon extends EquipableItem {
  protected float attackDamageModifier;
  public float getAttackDamageModifier(){return attackDamageModifier;};
  protected float hitChanceModifier;
  public float getHitChanceModifier(){return hitChanceModifier;};
  protected int condition;
  public boolean reduceCondition(int reduce) {
    condition -= reduce;
    if (condition <= 0 ){
      return false;
    }
    return true;
  }
  public int getCondition(){return condition;};

  public Weapon(Game game) {
    super(game);
  }
}
