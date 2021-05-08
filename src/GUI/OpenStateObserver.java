package GUI;

import items.inventory.InventoryOpenState;

public interface OpenStateObserver {
    void update(InventoryOpenState invOp);
}
