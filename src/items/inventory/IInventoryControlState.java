package items.inventory;

import util.math.Vec;

/**
 * Interface for a state of the statemachine used to navigate the inventory.
 */
public interface IInventoryControlState {
    /**
     * Handle input to navigate the inventory.
     * @param inventory The inventory to navigate
     * @return The next state (if the state should be changed) or null
     */
    IInventoryControlState handleInput(Inventory inventory);

    /**
     * Any action which should be performed when entering the state.
     * @param inventory The inventory to navigate
     */
    default void enter(Inventory inventory) { };

    /**
     * Any action which should be performed when exiting the state.
     * @param inventory The inventory to navigate
     */
    default void exit(Inventory inventory) { };

    /**
     * Check, if the opener of the inventory left the area around the inventory.
     * @param inventory The inventory to navigate.
     * @return True, if the owner left.
     */
    default boolean openerLeft(Inventory inventory) {
        Vec openerPosition = new Vec(inventory.getOpener().getPosition());
        Vec ownPosition = new Vec(inventory.parent.getPosition());

        Vec diff = openerPosition.subtract(ownPosition);
        float distance = diff.magnitude();
        if (distance > inventory.getLeavingDistanceThreshold()) {
            return true;
        }
        return false;
    }
}
