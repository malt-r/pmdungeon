package items.inventory;

import java.util.logging.Logger;

public class InventoryClosedState implements IInventoryControlState {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // TODO: find real usage for this..
    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        // TODO: how to prevent all inventories (from chests and stuff) to pop open, if
        // I is pressed? -> this needs to be in hero
        //if (Gdx.input.isKeyJustPressed((Input.Keys.I))){
            ///nextState = new InventoryOpenState();
        //}
        return nextState;
    }

    @Override
    public void enter(Inventory inventory) {
        mainLogger.info("Closing inventory");
        inventory.getOpener().unlock();
    }
}
