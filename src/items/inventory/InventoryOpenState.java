package items.inventory;

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
        else if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
            nextState = new InventorySelectState(this.selectorIdx);
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
            nextState = new InventoryClosedState();
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.L))) {
            mainLogger.info("Logging inventory");
            inventory.logContent();
        }
        if (prevIdx != this.selectorIdx) {
            logCurrentSelection(inventory);
        }

        return nextState;
    }

    @Override
    public void enter(Inventory inventory) {
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
        mainLogger.info("Selected item at index " + this.selectorIdx + " :");
        inventory.getItemAt(this.selectorIdx).accept(inventory.itemLogger);
    }

    private void printUsage() {
        mainLogger.info("Usage: Arrow Keys (Move Selection), E (Select), L (Log Content), ESC (Exit)");
    }

    public void exit(Inventory inventory) {
        mainLogger.info("Closing inventory");
    };
}
