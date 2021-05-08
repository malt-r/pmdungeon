package items.inventory;

import GUI.OpenStateObserver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import items.Item;
import items.IInventoryOpener;

import java.util.logging.Logger;

// TODO: detect that the opener is moving away from this
public class OtherInventoryOpenState extends InventoryOpenState {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public OtherInventoryOpenState() {
        this.selectorIdx = -1;
    }

    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        var nextState = super.handleInput(inventory);

        if (null == nextState && !lockInput) {
            if (Gdx.input.isKeyJustPressed((Input.Keys.E))) { // take
                transferItemToOpenersInventory(inventory);
                notifyObservers();
                //nextState = new InventoryOpenState(this.selectorIdx);
            }
            else if (Gdx.input.isKeyJustPressed((Input.Keys.I))) {
                // inspect?
                var item = inventory.getItemAt(this.selectorIdx);
                mainLogger.info("Inspecting Item:");
                item.accept(inventory.itemLogger);
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                // take all
                takeAll(inventory);
            }
        }
        return nextState;
    }

    private void transferItemToOpenersInventory(Inventory inventory) {
        if (this.selectorIdx < inventory.getCount() && null != inventory.getItemAt(this.selectorIdx)) {
            mainLogger.info("transfering item to openers inventory");

            Item item = inventory.getItemAt(this.selectorIdx);
            boolean couldAddItem = inventory.getOpener().addItemToInventory(item);
            if (couldAddItem) {
                inventory.removeAt(this.selectorIdx);
            } else {
                mainLogger.info("Could not transfer item to openers inventory, it probably is full");
            }
            if (this.selectorIdx > 0) {
                this.selectorIdx -= 1;
            }
        }
    }

    private void takeAll(Inventory inventory) {
        IInventoryOpener opener = inventory.getOpener();
        if (inventory.getCount() > opener.getNumFreeSlotsOfInventory()) {
            mainLogger.info("You can not take all items, not enough space");
        } else {
            mainLogger.info("Taking it all");
            Item item;

            // removing one item will shift all other item one index to the left,
            // so always remove the one on idx 0
            int count = inventory.getCount();
            for (int i = 0; i < count; i++) {
                item = inventory.removeAt(0);
                opener.addItemToInventory(item);
            }
        }
    }

    @Override
    protected void printUsage() {
        super.printUsage();
        mainLogger.info("I (inspect item); E (take), R (take all)");
    }
}
