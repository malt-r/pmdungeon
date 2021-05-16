package GUI;

import items.inventory.InventoryOpenState;

/**
 * Interface for the openstate observer
 */
public interface OpenStateObserver {
    /**
     * Updates a openstate observer with the inventory open state.
     * @param invOp inventoryopenstate which is given in the update
     */
    void update(InventoryOpenState invOp);
}
