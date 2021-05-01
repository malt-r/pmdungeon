package InventorySystem;

public class SpecificPotion2 extends Potion {
    @Override
    public String getName() {
        return "SpecificPotion2";
    }

    @Override
    public void accept(IItemVisitor visitor) {
        visitor.visit(this);
    }
}
