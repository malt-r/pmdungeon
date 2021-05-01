package InventorySystem;

// dummy implementation
public abstract class Item {
    public boolean isStackable() {
        return true;
    }
    public abstract String getName();
    public abstract void accept(IItemVisitor visitor);
    // dummy
    public String getDescription() {
        return "dummy";
    }
}
