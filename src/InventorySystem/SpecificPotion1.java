package InventorySystem;

public class SpecificPotion1 extends Potion {
    @Override
    public String getName() {
        return "SpecificPotion1";
    }

    @Override
    public void accept(IItemVisitor visitor) {
        visitor.visit(this);
    }
}
