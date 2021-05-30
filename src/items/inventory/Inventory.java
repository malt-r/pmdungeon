package items.inventory;

import GUI.InventoryObserver;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.IInventoryOpener;
import items.Item;
import items.ItemLogger;
import main.Game;
import main.Hero;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Implements an inventory. Basically a wrapper around an ArrayList. Uses a statemachine for
 * navigation through the inventory.
 *
 * @param <T> Subclass of Item
 */
public class Inventory<T extends Item> implements ObservableInventory {
  /** Logger */
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // maximum capacity
  private final int capacity;
  // stores the effective item data
  private final ArrayList<T> items;
  private final ArrayList<InventoryObserver> observerList = new ArrayList<InventoryObserver>();
  private final ArrayList<InventoryObserver> observersToRemove = new ArrayList<>();
  /** Specific logger for Items. */
  protected items.ItemLogger itemLogger;
  /** The parent of this inventory. Used to get the position on which items should be dropped. */
  protected IDrawable parent;
  /** The current opener of an inventory. Used to transfer items to another inventory. */
  protected items.IInventoryOpener inventoryOpener;
  /**
   * The distance which the opener can move away from this inventory before the inventory is closed.
   */
  protected float leavingDistanceThreshold = 1.f;
  /** The current state of the state machine for navigating this inventory. */
  protected IInventoryControlState currentState;

  /**
   * Constructor
   *
   * @param parent The parent of this inventory, used to get the position.
   * @param capacity The maximum capacity of the inventory.
   */
  public Inventory(IDrawable parent, int capacity) {
    items = new ArrayList<>();
    this.parent = parent;
    this.capacity = capacity;
    this.currentState = new InventoryClosedState();
    this.itemLogger = new items.ItemLogger();
  }

  /**
   * Gets the current opener of the inventory.
   *
   * @return The current opener.
   */
  public items.IInventoryOpener getOpener() {
    return inventoryOpener;
  }

  /**
   * Returns the leavingDistanceThreshold
   *
   * @return the leavingDistanceThreshold
   */
  public float getLeavingDistanceThreshold() {
    return leavingDistanceThreshold;
  }

  /**
   * The count of items currenlty in this inventory.
   *
   * @return the count of items.
   */
  public int getCount() {
    return items.size();
  }

  /**
   * Adds an item to the inventory.
   *
   * @param item The item to be added.
   * @return True, if the item could be added, otherwise false.
   */
  public boolean addItem(T item) {
    if (this.getCount() < this.capacity) {
      mainLogger.info("Adding item: " + item.getName());
      boolean success = items.add(item);
      notifyObservers();
      return success;
    }
    return false;
  }

  /**
   * Get a position at specific index of the inventory.
   *
   * @param index The index of the item.
   * @return The item at index.
   */
  public T getItemAt(int index) {
    if (items.size() <= index) {
      return null;
    } else {
      return items.get(index);
    }
  }

  /**
   * Remove an item from a specific index from the inventory.
   *
   * @param index The index of the item.
   * @return The removed item.
   */
  public T removeAt(int index) {
    if (items.size() <= index) {
      throw new IndexOutOfBoundsException();
    } else {
      // this will shift all indices after 'index' to the left (minus 1)
      T item = items.remove(index);
      // dropItem(item);
      notifyObservers();
      return item;
    }
  }

  public boolean dropItem(Item itemToDrop) {
    try {
      var parentPos = this.parent.getPosition();
      var finalX = Math.round(parentPos.x);
      var finalY = Math.round(parentPos.y);
      var point = new Point(finalX, finalY);
      itemToDrop.setPosition(point);
      Game.getInstance().addEntity(itemToDrop);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

  public boolean dropItemFromIdx(int idx) {
    Item item = null;
    try {
      item = this.removeAt(idx);
    } catch (IndexOutOfBoundsException ex) {
      return false;
    }
    dropItem(item);
    mainLogger.info("Dropped " + item.getName() + " from inventory");
    return true;
  }

  /** Removes all items from this inventory and closes it. */
  public void clear() {
    transitionToNextState(new InventoryClosedState());
    items.clear();
    notifyObservers();
  }

  /**
   * Perform state transition to nextState and notify observers.
   *
   * @param nextState the state to transition to.
   */
  protected void transitionToNextState(IInventoryControlState nextState) {
    this.currentState.exit(this);
    nextState.enter(this);
    this.currentState = nextState;
    notifyObservers();
  }

  /** Should be called periodically in update method of parent. */
  public void update() {
    var nextState = this.currentState.handleInput(this);

    if (null != nextState) {
      transitionToNextState(nextState);
    }

    removeObserversToRemove();
  }

  // check, if the opener is the parent of this inventory
  private boolean isOpenerParent(IInventoryOpener opener) {
    return opener.equals(this.parent);
  }

  /**
   * Gets the number of free slots.
   *
   * @return number of free slots.
   */
  public int getNumFreeSlots() {
    return this.capacity - this.items.size();
  }

  /**
   * Gets the total capacity of the inventory.
   *
   * @return The capacity.
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Opens the inventory -> transition to InventoryOpenState If the opener is the parent of the
   * inventory the OwnInventoryOpenState will be the next state. If the opener is not the parent of
   * the inventory, the OtherInventoryOpenState will be the next state.
   *
   * @param opener The opener of the inventory.
   */
  public void open(IInventoryOpener opener) {
    this.inventoryOpener = opener;
    mainLogger.info(
        "This: " + this.parent.toString() + " Opener: " + this.inventoryOpener.toString());

    IInventoryControlState nextState;
    if (this.getCount() == 0) {
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

  /** Log the current contents of the inventory. */
  public void logContent() {
    mainLogger.info("Inventory content:");

    for (int i = 0; i < this.items.size(); i++) {
      var item = items.get(i);
      mainLogger.info("IDX: " + i);
      item.accept(this.itemLogger);
    }
  }

  /**
   * Gets the item logger.
   *
   * @return the item logger.
   */
  public ItemLogger getItemLogger() {
    return this.itemLogger;
  }

  /** gets the current state of the state machine */
  public IInventoryControlState getCurrentState() {
    return currentState;
  }

  /**
   * Registers an observer
   *
   * @param observer to be registered
   */
  @Override
  public void register(InventoryObserver observer) {
    this.observerList.add(observer);
  }

  /**
   * Unregisters an observer
   *
   * @param observer to be unregistered
   */
  @Override
  public void unregister(InventoryObserver observer) {
    if (this.observerList.contains(observer) && !this.observersToRemove.contains(observer)) {
      this.observersToRemove.add(observer);
    }
  }

  private void removeObserversToRemove() {
    for (InventoryObserver observer : observersToRemove) {
      this.observerList.remove(observer);
    }
    this.observersToRemove.clear();
  }

  /** notifies all observers */
  @Override
  public void notifyObservers() {
    for (InventoryObserver obs : observerList) {
      if (!observersToRemove.contains(obs)) {
        obs.update(this, this.parent instanceof Hero);
      }
    }
  }
}
