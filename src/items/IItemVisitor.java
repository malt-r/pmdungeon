package items;

import items.potions.HealthPotion;
import items.scrolls.AttackScroll;
import items.weapons.RegularSword;
import items.weapons.Weapon;

// sample implementation
public interface IItemVisitor {
  // fallback, if no specific implementation is provided
  default void visit(Item item){ };
  void visit(Weapon weapon);
  void visit(HealthPotion potion);
  void visit(AttackScroll scroll);
}
