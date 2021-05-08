package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.scrolls.SupervisionScroll;
import items.shields.EagleShield;
import items.shields.WoodShield;
import items.weapons.RegularSword;
import items.weapons.Spear;
/**
 * Factory for creation of monster
 * <p>
 *     Creates all types of monster that can appear in the game.
 * </p>
 */
public class ItemFactory {
  /**
   *
   * @param   itemType    Type of the item
   * @throws  Exception   if itemType is not supported
   * @return  A monster, of the specified type.
   */
  public static Item CreateItem (ItemType itemType) throws Exception{
    if(itemType == ItemType.SWORD_REGULAR)      return new RegularSword();
    if(itemType == ItemType.SPEAR_REGULAR)      return new Spear();
    if(itemType == ItemType.POTION_HEAL)        return new HealthPotion();
    if(itemType == ItemType.POTION_POISON)      return new PoisonPotion();
    if(itemType == ItemType.SCROLL_ATTACK)      return new AttackScroll();
    if(itemType == ItemType.SCROL_SUPERVISION)  return new SupervisionScroll();
    if(itemType == ItemType.SCROLL_SPEED)       return new SpeedScroll();
    if(itemType == ItemType.SHIELD_WOOD)        return new WoodShield();
    if(itemType == ItemType.SHIELD_EAGLE)       return new EagleShield();
    throw new Exception("ItemType no supported");
  }
}