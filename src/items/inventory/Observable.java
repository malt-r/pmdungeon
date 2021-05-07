package items.inventory;

import GUI.InventoryObserver;

public interface Observable {
    void register(InventoryObserver observer);
    void unregister(InventoryObserver observer);
    void notifyObservers();

}
