package items.weapons;

import items.EquipableItem;
import main.Game;

public abstract class Weapon extends EquipableItem {
  protected float attackDamageModifier;
  public float getAttackDamageModifier(){return attackDamageModifier;};
  protected float hitChanceModifier;
  public float getHitChanceModifier(){return hitChanceModifier;};
  protected int condition;
  public float getCondition(){return condition;};

  public Weapon(Game game) {
    super(game);
  }
}
