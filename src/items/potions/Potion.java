package items.potions;

import items.UseableItem;
import main.Game;
/**
 *  Abstract Potion class.
 * <p>
 *   Contains everything that describes a generic potion and is used to group all potions.
 * </p>
 */
public abstract class Potion extends UseableItem {
  public Potion(Game game) {
    super(game);
  }
}
