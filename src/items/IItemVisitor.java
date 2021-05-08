package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.scrolls.SupervisionScroll;
import items.shields.Shield;
import items.weapons.Weapon;

/**
 * This interface implements an visitor for all items in the game
 */
public interface IItemVisitor {
  /**
   * Fallback if no concrete implementation exists.
   * @param item item which should be visited.
   */
  default void visit(Item item){ };

  /**
   * Visits the weapon
   * @param weapon weapon which should be visited
   */
  void visit(Weapon weapon);
  /**
   * Visits the shield
   * @param shield weapon which should be visited
   */
  void visit(Shield shield);
  /**
   * Visits the potion
   * @param potion weapon which should be visited
   */
  void visit(HealthPotion potion);
  /**
   * Visits the potion
   * @param potion weapon which should be visited
   */
  void visit(PoisonPotion potion);
  /**
   * Visits the scroll
   * @param scroll weapon which should be visited
   */
  void visit(AttackScroll scroll);
  /**
   * Visits the scroll
   * @param scroll scroll which should be visited
   */
  void visit(SpeedScroll scroll);

  /**
   * Visists the scroll
   * @param scroll scroll shich should be visited
   */
  void visit(SupervisionScroll scroll);
}
