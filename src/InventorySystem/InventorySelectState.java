package InventorySystem;

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
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
            nextState = new InventoryOpenState(this.selectorIdx);
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.D))) {
            dropItem(inventory);
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
            // equip, use item
            //inventory.Opener.visit(inventory.getItemStackAt(this.selectorIdx));
            //inventory.getItemStackAt(this.selectorIdx).pop(1);
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.I))) {
            // inspect?
            var stack = inventory.getItemStackAt(this.selectorIdx);
            inventory.itemLogger.visit(stack.getItem());
        }
        return nextState;
    }

    // TODO: this should drop the items into the world.. not just the void that is the garbage collector
    private void dropItem(Inventory inventory) {
        // drop number of items (one of stack or all), check, if selectorIdx needs to be
        // decremented

        var stack = inventory.getItemStackAt(this.selectorIdx);
        int poppedItems = stack.pop(1);

        mainLogger.info("Dropped " + poppedItems + " times " + stack.getItemName() + " from inventory");
        if (stack.getCount() <= 0) {
            inventory.removeStackAt(this.selectorIdx);
            this.selectorIdx -= 1;
        }
    }
}
