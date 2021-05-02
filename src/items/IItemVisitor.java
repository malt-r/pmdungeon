package items;

import items.potions.HealthPotion;
import items.scrolls.AttackScroll;
import items.weapons.RegularSword;

// sample implementation
public interface IItemVisitor {
  // fallback, if no specific implementation is provided
  default void visit(Item item){ };
  void visit(RegularSword sword);
  void visit(HealthPotion potion);
  void visit(AttackScroll scroll);
}
