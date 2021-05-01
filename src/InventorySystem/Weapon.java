package InventorySystem;

public abstract class Weapon extends Item {
    @Override
    public String getName() {
        return "Generic Sword";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
