package GUI;

import items.inventory.Inventory;

public interface InventoryObserver extends Observer {
    void update(Inventory inv, boolean fromHero);
}
