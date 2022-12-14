package items.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.logging.Logger;

/**
 * The empty state of the inventory.
 */
public class InventoryEmptyState implements InventoryControlState {
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void enter(Inventory inventory) {
    mainLogger.info("Inventory is empty, close with ESC");
  }

  @Override
  public InventoryControlState handleInput(Inventory inventory) {
    InventoryControlState nextState = null;
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      nextState = new InventoryClosedState();
    }

    if (openerLeft(inventory)) {
      nextState = new InventoryClosedState();
    }
    return nextState;
  }
}
