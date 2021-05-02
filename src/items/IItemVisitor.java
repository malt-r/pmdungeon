package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.shields.Shield;
import items.weapons.Weapon;

// sample implementation
public interface IItemVisitor {
  // fallback, if no specific implementation is provided
  default void visit(Item item){ };
  void visit(Weapon weapon);
  void visit(Shield shield);
  void visit(HealthPotion potion);
  void visit(PoisonPotion potion);
  void visit(AttackScroll scroll);
  void visit(SpeedScroll scroll);
}
