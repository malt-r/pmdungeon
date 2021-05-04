package items.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.logging.Logger;

public class InventoryEmptyState implements IInventoryControlState {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void enter(Inventory inventory) {
        mainLogger.info("Inventory is empty, close with ESC");
    }

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            nextState = new InventoryClosedState();
        }
        return nextState;
    }
}
