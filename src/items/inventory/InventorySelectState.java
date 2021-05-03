package items.inventory;

import items.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.logging.Logger;

public class InventorySelectState implements IInventoryControlState {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    int selectorIdx;
    public InventorySelectState(int selectorIdx) {
        this.selectorIdx = selectorIdx;
    }
    @Override
    public void enter(Inventory inventory) {
        logSelectedItem(inventory);
        printUsage();
    }

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
            nextState = new InventoryOpenState(this.selectorIdx);
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.Q))) {
            dropItem(inventory);
            nextState = new InventoryOpenState(this.selectorIdx);
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
            useItem(inventory);
            nextState = new InventoryOpenState(this.selectorIdx);
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.I))) {
            // inspect?
            var item = inventory.getItemAt(this.selectorIdx);
            item.accept(inventory.itemLogger);
        }
        else if (Gdx.input.isKeyJustPressed((Input.Keys.M))) {
            // swap item positions or put item in bag
        }
        return nextState;
    }

    private void useItem(Inventory inventory) {
        if (this.selectorIdx < inventory.getCount() && null != inventory.getItemAt(this.selectorIdx)) {
            var item = inventory.removeAt(this.selectorIdx);
            this.selectorIdx -= 1;

            mainLogger.info("using item:");
            item.accept(inventory.itemLogger);
            item.accept(inventory.getOpener());
        }
    }

    private void logSelectedItem(Inventory inventory) {
        Item item = inventory.getItemAt(this.selectorIdx);
        mainLogger.info("Selected item: " + item.getName());
    }

    private void printUsage() {
        mainLogger.info("Options: ESC (unselect item); Q (drop); I (inspect item); E (use)");
    }

    private void dropItem(Inventory inventory) {

        if (this.selectorIdx < inventory.getCount() && null != inventory.getItemAt(this.selectorIdx)) {
            var item = inventory.removeAt(this.selectorIdx);
            this.selectorIdx -= 1;

            mainLogger.info("Dropped " + item.getName() + " from inventory");
            // TODO: drop back into world

        }
    }
}
