package InventorySystem.TestItems;

import InventorySystem.IItemVisitor;

public abstract class Weapon extends Item {
    @Override
    public String getName() {
        return "Generic Sword";
    }
    @Override
    public abstract void accept(IItemVisitor visitor);
}
