package InventorySystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.logging.Logger;

public class InventoryOpenState implements IInventoryControlState{
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // organize inventory as a pseudo-grid
    int gridWidth = 6;
    int selectorIdx;

    public InventoryOpenState() {
        this.selectorIdx = -1;
    }

    public InventoryOpenState(int selectorIdx) {
        this.selectorIdx = selectorIdx;
    }

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;

        if (Gdx.input.isKeyJustPressed((Input.Keys.W))){
            if (this.selectorIdx >= gridWidth) {
                this.selectorIdx -= gridWidth;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (this.selectorIdx > 0) {
                this.selectorIdx -= 1;
            }
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.S))) {
            if (inventory.getCount() >= this.selectorIdx + gridWidth) {
                this.selectorIdx += gridWidth;
            }
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.D))) {
            if (this.selectorIdx - 1 < inventory.getCount()) {
                this.selectorIdx += 1;
            }
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
            nextState = new InventorySelectState(this.selectorIdx);
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
            nextState = new InventoryClosedState();
        }
        return nextState;
    }

    @Override
    public void enter(Inventory inventory) {
        int inventoryCount = inventory.getCount();
        if (inventoryCount == 0) {
            this.selectorIdx = -1;
        } else if (this.selectorIdx <= 0) { // if this was set before, keep the index
            this.selectorIdx = 0;
        }

        mainLogger.info("Opened Inventory, printing contents:");
        // TODO: this could use an inventory formatter
        inventory.logContent();
    };

    private void printUsage() {
        mainLogger.info("Currently in open mode.");
        mainLogger.info("Move selection with WASD");
        mainLogger.info("Select item with E");
    }

    public void exit(Inventory inventory) {
        mainLogger.info("Closing inventory");
    };
}
