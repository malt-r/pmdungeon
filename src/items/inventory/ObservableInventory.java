package items.inventory;

import GUI.InventoryObserver;

public interface ObservableInventory {
  void register(InventoryObserver observer);

  void unregister(InventoryObserver observer);

  void notifyObservers();
}
