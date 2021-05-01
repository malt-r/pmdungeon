package InventorySystem;

// sample implementation
public interface IItemVisitor {
    // fallback, if no specific implementation is provided
    default void visit(Item item){ };
    void visit(Spear1 spear);
    void visit(Sword1 sword);
    void visit(SpecificPotion1 potion);
    void visit(SpecificPotion2 potion);
}
