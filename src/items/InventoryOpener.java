package items;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The inventory opener.
 */
public interface InventoryOpener extends ItemVisitor {
  boolean addItemToInventory(Item item);

  int getNumFreeSlotsOfInventory();

  default boolean lock() {
    return true;
  }

  default boolean unlock() {
    return true;
  }

  Point getPosition();
}
