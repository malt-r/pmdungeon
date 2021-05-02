package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.shields.EagleShield;
import items.shields.WoodShield;
import items.weapons.RegularSword;
import items.weapons.Spear;
import main.Game;
/**
 * Factory for creation of monster
 * <p>
 *     Creates all types of monster that can appear in the game.
 * </p>
 */
public class ItemFactory {
  /**
   * Normalize the difference-vector between two Points on a defined basis.
   *
   * @param   itemType    Type of the item
   * @param   game        The Game where the monster appears
   * @throws  Exception   if itemType is not supported
   * @return  A monster, of the specified type.
   */
  public static Item CreateItem (ItemType itemType, Game game) throws Exception{
    if(itemType == ItemType.SWORD_REGULAR)  return new RegularSword(game);
    if(itemType == ItemType.SPEAR_REGULAR)  return new Spear(game);
    if(itemType == ItemType.POTION_HEAL)    return new HealthPotion(game);
    if(itemType == ItemType.POTION_POISON)  return new PoisonPotion(game);
    if(itemType == ItemType.SCROLL_ATTACK)  return new AttackScroll(game);
    if(itemType == ItemType.SCROLL_SPEED)   return new SpeedScroll(game);
    if(itemType == ItemType.SHIELD_WOOD)  return new WoodShield(game);
    if(itemType == ItemType.SHIELD_EAGLE)   return new EagleShield(game);
    throw new Exception("ItemType no supported");
  }
}