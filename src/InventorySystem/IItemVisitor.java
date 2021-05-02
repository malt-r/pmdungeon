package InventorySystem;

import InventorySystem.TestItems.*;

// sample implementation
public interface IItemVisitor {
    // fallback, if no specific implementation is provided
    default void visit(Spear1 spear){};
    default void visit(Sword1 sword){};
    default void visit(SpecificPotion1 potion){};
    default void visit(SpecificPotion2 potion){};
    default void visit(Weapon weapon){};
    default void visit(Potion potion){};
    default void visit(Item item) {}
}
