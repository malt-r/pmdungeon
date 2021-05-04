package items.inventory;

import util.math.Vec;

public interface IInventoryControlState {
    IInventoryControlState handleInput(Inventory inventory);
    default void enter(Inventory inventory) { };
    default void exit(Inventory inventory) { };

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
