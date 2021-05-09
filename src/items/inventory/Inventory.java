package items.inventory;

import GUI.InventoryObserver;
import items.*;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
//TODO - is this necessary?
import main.Hero;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Inventory<T extends Item> implements ObservableInventory{
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected items.ItemLogger itemLogger;
    protected IDrawable parent;

    protected items.IInventoryOpener inventoryOpener;
    public items.IInventoryOpener getOpener() {
        return inventoryOpener;
    }
    protected float leavingDistanceThreshold = 0.7f;

    public float getLeavingDistanceThreshold() {
        return leavingDistanceThreshold;
    }

    public int getCount() {
        return items.size();
    }

    protected IInventoryControlState currentState;

    private final int capacity;
    private final ArrayList<T> items;

    private ArrayList<InventoryObserver> observerList = new ArrayList<InventoryObserver>();

    public Inventory(IDrawable parent, int capacity) {
        items = new ArrayList<>();
        this.parent = parent;
        this.capacity = capacity;
        this.currentState = new InventoryClosedState();
        this.itemLogger = new items.ItemLogger();
    }

    public boolean addItem(T item) {
        if (this.getCount() < this.capacity) {
            mainLogger.info("Adding item: " + item.getName());
            boolean success = items.add(item);
            notifyObservers();
            return success;
        }
        return false;
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
            notifyObservers();
            return item;
        }
    }

    private void dropItem(Item item){
        item.setPosition(parent.getPosition());
    }

    public void clear() {
        transitionToNextState(new InventoryClosedState());
        items.clear();
        notifyObservers();
    }

    protected void transitionToNextState(IInventoryControlState nextState) {
        this.currentState.exit(this);
        nextState.enter(this);
        this.currentState = nextState;
        notifyObservers();
    }

    public void update() {
        var nextState = this.currentState.handleInput(this);

        if (null != nextState) {
            transitionToNextState(nextState);
        }
    }

    private boolean isOpenerParent(IInventoryOpener opener) {
        return opener.equals(this.parent);
    }

    public int getNumFreeSlots() {
        return this.capacity - this.items.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public void open(IInventoryOpener opener) {
        this.inventoryOpener = opener;
        mainLogger.info("This: " + this.parent.toString() + " Opener: " + this.inventoryOpener.toString());

        IInventoryControlState nextState;
        if (this.getCount() == 0){
            nextState = new InventoryEmptyState();
        } else {
            boolean openerIsParent = isOpenerParent(opener);
            if (openerIsParent) {
                nextState = new OwnInventoryOpenState();
            } else {
                nextState = new OtherInventoryOpenState();
            }
        }

        transitionToNextState(nextState);
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

    public IInventoryControlState getCurrentState() { return currentState; }

    @Override
    public void register(InventoryObserver observer){
        this.observerList.add(observer);
    }

    @Override
    public void unregister(InventoryObserver observer){
        this.observerList.remove(observer);
    }

    @Override
    public void notifyObservers(){
        for (InventoryObserver obs : observerList){
            obs.update(this, this.parent instanceof Hero);
        }
    }
}
