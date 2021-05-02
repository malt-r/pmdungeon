package InventorySystem.TestItems;

import InventorySystem.IItemVisitor;

public abstract class Potion extends Item {
    @Override
    public String getName() {
        return "Generic Potion";
    }
    @Override
    public abstract void accept(IItemVisitor visitor);
}
