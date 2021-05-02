package InventorySystem.TestItems;

import InventorySystem.IItemVisitor;
import InventorySystem.TestItems.Weapon;

public class Sword1 extends Weapon {
    @Override
    public String getName() {
        return "Sword1";
    }

    @Override
    public void accept(IItemVisitor visitor) {
        visitor.visit(this);
    }
}
