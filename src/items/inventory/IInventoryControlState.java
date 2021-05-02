package items.inventory;

public interface IInventoryControlState {
    IInventoryControlState handleInput(Inventory inventory);
    default void enter(Inventory inventory) { };
    default void exit(Inventory inventory) { };
}
