package InventorySystem.TestItems;

import InventorySystem.IItemVisitor;
import InventorySystem.TestItems.Weapon;

public class Spear1 extends Weapon {
    @Override
    public String getName() {
        return "Spear1";
    }

    @Override
    public void accept(IItemVisitor visitor) {
        visitor.visit(this);
    }
}
