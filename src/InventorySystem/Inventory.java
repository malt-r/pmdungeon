package InventorySystem;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Inventory {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected ItemLogger itemLogger;

    protected IInventoryControlState currentState;
    protected IDrawable parent;

    private int capacity;
    private ArrayList<ItemStack> items;

    public Inventory(IDrawable parent, int capacity) {
        items = new ArrayList<ItemStack>();
        this.capacity = capacity;
        this.currentState = new InventoryClosedState();
    }

    private <T extends Item> boolean addNewStack(T item, int count) {
        if (this.items.size() < this.capacity) {
            items.add(new ItemStack(item, count));
        } else {
            mainLogger.info("Capacity of inventory already reached");
            return false;
        }
        return true;
    }

    public <T extends Item> boolean addItem(T item, int count) {
        var itemType = item.getClass();
        if (item.isStackable()) {
            ItemStack foundStack =
                    items.stream().filter(
                            itemStack ->
                                    itemType.equals(itemStack.getItemType()))
                            .findAny()
                            .orElse(null);
            if (null == foundStack) {
                return addNewStack(item, count);
            } else {
                foundStack.push(count);
            }
        } else {
            return addNewStack(item, count);
        }
        return true;
    }

    public ItemStack getItemStackAt(int index) {
        if (items.size() <= index) {
            throw new IndexOutOfBoundsException();
        } else {
            return items.get(index);
        }
    }

    // TODO:
    // where to put drop logic?
    // selectorIdx in InventorySelectState needs to update, if the stack was removed from inventory
    /*public void dropAmountOfStackAt(int index, int count, ) {
        var stack = getItemStackAt(index);
        int poppedCount = stack.pop(count);

        // TODO: place instances of items in world at location

    }*/

    public ItemStack removeStackAt(int index) {
        if (items.size() <= index) {
            throw new IndexOutOfBoundsException();
        } else {
            var stack = items.get(index);
            // this will shift all indices after 'index' to the left (minus 1)
            items.remove(index);
            return stack;
        }
    }

    public void update() {
        var nextState = this.currentState.handleInput(this);

        if (null != nextState) {
            this.currentState.exit(this);
            nextState.enter(this);
            this.currentState = nextState;
        }
    }

    public void open(IItemVisitor opener) {
        //this.opener = opener;

        var nextState = new InventoryOpenState();
        this.currentState.exit(this);
        nextState.enter(this);
        this.currentState = nextState;
    }

    public void logContent() {
        // TODO, real implementation;
        mainLogger.info("Inventory content:");

        for (ItemStack stack : items) {
            mainLogger.info(stack.getItemName() + " : count = " + stack.getCount());
        }
    }

    public int getCount() {
        return items.size();
    }

    public ItemLogger getItemLogger() {
        return this.itemLogger;
    }
}
