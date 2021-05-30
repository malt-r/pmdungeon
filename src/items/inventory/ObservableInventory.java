package items.inventory;

import gui.InventoryObserver;

/**
 * ObservableInventory
 */
public interface ObservableInventory {
  void register(InventoryObserver observer);

  void unregister(InventoryObserver observer);

  void notifyObservers();
}
