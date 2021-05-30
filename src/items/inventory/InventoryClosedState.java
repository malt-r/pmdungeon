package items.inventory;

import java.util.logging.Logger;

/** The state of the closed inventory. */
public class InventoryClosedState implements InventoryControlState {
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public InventoryControlState handleInput(Inventory inventory) {
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
