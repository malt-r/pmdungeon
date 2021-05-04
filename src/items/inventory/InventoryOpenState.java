package items.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.logging.Logger;

public abstract class InventoryOpenState implements IInventoryControlState{
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // organize inventory as a pseudo-grid
    protected int gridWidth = 6;
    protected int selectorIdx;
    protected boolean lockInput;
    protected int lockCounter;

    public InventoryOpenState() {
        this.selectorIdx = -1;
    }

    public InventoryOpenState(int selectorIdx) {
        this.selectorIdx = selectorIdx;
    }

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;

        // prevent direct switch to selection on first update after opening
        if (!lockInput) {
            int prevIdx = this.selectorIdx;
            if (Gdx.input.isKeyJustPressed((Input.Keys.UP))){
                if (this.selectorIdx > gridWidth) {
                    this.selectorIdx -= gridWidth;
                }
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                if (this.selectorIdx > 0) {
                    this.selectorIdx -= 1;
                }
            }
            else if (Gdx.input.isKeyJustPressed((Input.Keys.DOWN))) {
                if (inventory.getCount() > this.selectorIdx + gridWidth) {
                    this.selectorIdx += gridWidth;
                }
            }
            else if (Gdx.input.isKeyJustPressed((Input.Keys.RIGHT))) {
                if ((this.selectorIdx + 1) < inventory.getCount()) {
                    this.selectorIdx += 1;
                }
            }
            else if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
                mainLogger.info("Closing inventory");
                nextState = new InventoryClosedState();
            }
            else if (Gdx.input.isKeyJustPressed((Input.Keys.L))) {
                mainLogger.info("Logging inventory");
                inventory.logContent();
            }
            if (prevIdx != this.selectorIdx) {
                logCurrentSelection(inventory);
            }
        }

        if (lockInput) {
            lockCounter += 1;
            if (lockCounter >= 3) {
                lockInput = false;
            }
        }
        if (inventory.getCount() == 0) {
            nextState = new InventoryEmptyState();
        }
        return nextState;
    }

    @Override
    public void enter(Inventory inventory) {
        inventory.getOpener().lock();
        lockInput = true;
        lockCounter = 0;
        mainLogger.info("OPENED INVENTORY");
        printUsage();
        int inventoryCount = inventory.getCount();
        if (inventoryCount == 0) {
            this.selectorIdx = -1;
        } else if (this.selectorIdx <= 0) { // if this was set before, keep the index
            this.selectorIdx = 0;
        }

        logCurrentSelection(inventory);
    };

    private void logCurrentSelection(Inventory inventory) {
        if (this.selectorIdx >= 0) {
            mainLogger.info("Selected item at index " + this.selectorIdx + " :");
            inventory.getItemAt(this.selectorIdx).accept(inventory.itemLogger);
        } else {
            mainLogger.info("Inventory is empty");
        }
    }

    protected void printUsage() {
        mainLogger.info("Usage: Arrow Keys (Move Selection), L (Log Content), ESC (Exit)");
    }

    public void exit(Inventory inventory) {
        mainLogger.info("Leaving open state");
    };
}
