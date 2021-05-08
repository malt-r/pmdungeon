package GUI;

import items.inventory.Inventory;

public interface InventoryObserver{
    void update(Inventory inv, boolean fromHero);
}
