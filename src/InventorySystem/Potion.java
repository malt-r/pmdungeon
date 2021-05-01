package InventorySystem;

public abstract class Potion extends Item {
    @Override
    public String getName() {
        return "Generic Potion";
    }

    @Override
    public void accept(IItemVisitor visitor) {

    }
}
