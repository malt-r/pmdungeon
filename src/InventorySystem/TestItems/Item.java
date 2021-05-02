package InventorySystem.TestItems;

import InventorySystem.IItemVisitor;

// dummy implementation
public abstract class Item {
    public abstract String getName();
    public abstract void accept(IItemVisitor visitor);
    // dummy
    public String getDescription() {
        return "dummy";
    }
}
