package items.inventory;

import items.*;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Inventory<T extends Item> {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected items.ItemLogger itemLogger;
    protected IDrawable parent;

    protected items.IItemVisitor inventoryOpener;
    public items.IItemVisitor getOpener() {
        return inventoryOpener;
    }

    public int getCount() {
        return items.size();
    }

    protected IInventoryControlState currentState;

    private int capacity;
    private ArrayList<T> items;

    public Inventory(IDrawable parent, int capacity) {
        items = new ArrayList<T>();
        //items = new ArrayList<InventoryItem<T>>();
        this.parent = parent;
        this.capacity = capacity;
        this.currentState = new InventoryClosedState();
        this.itemLogger = new items.ItemLogger();
    }

    public  boolean addItem(T item) {
        var itemType = item.getClass();
        if (this.getCount() < this.capacity) {
            return items.add(item);
        }
        return true;
    }

    public T getItemAt(int index) {
        if (items.size() <= index) {
            return null;
        } else {
            return items.get(index);
        }
    }

    public T removeAt(int index) {
        if (items.size() <= index) {
            throw new IndexOutOfBoundsException();
        } else {
            // this will shift all indices after 'index' to the left (minus 1)
            T item = items.remove(index);
            dropItem(item);
            return item;
        }
    }

    private void  dropItem(Item item){
        item.setPosition(parent.getPosition());

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
        this.inventoryOpener = opener;

        var nextState = new InventoryOpenState();
        this.currentState.exit(this);
        nextState.enter(this);
        this.currentState = nextState;
    }

    public void logContent() {
        mainLogger.info("Inventory content:");

        for (int i = 0; i < this.items.size(); i++) {
            var item = items.get(i);
            mainLogger.info("IDX: "+ i);
            item.accept(this.itemLogger);
        }
    }

    public ItemLogger getItemLogger() {
        return this.itemLogger;
    }
}
