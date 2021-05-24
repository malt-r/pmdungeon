package items;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public interface IInventoryOpener extends IItemVisitor {
    boolean addItemToInventory(Item item);
    int getNumFreeSlotsOfInventory();
    default boolean lock(){ return true;}
    default boolean unlock(){return true;}
    Point getPosition();
}