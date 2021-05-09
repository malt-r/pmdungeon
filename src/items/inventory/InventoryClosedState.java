package items.inventory;

import java.util.logging.Logger;

public class InventoryClosedState implements IInventoryControlState {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        return null;
    }

    @Override
    public void enter(Inventory inventory) {
        mainLogger.info("Closing inventory");
        var opener = inventory.getOpener();
        if (null != opener) {
            opener.unlock();
        }
    }
}
