package items.inventory;

import GUI.InventoryObserver;
import GUI.Observer;

public interface ObservableInventory extends Observable{
    void register(InventoryObserver observer);
    void unregister(InventoryObserver observer);
    void notifyObservers();
}
