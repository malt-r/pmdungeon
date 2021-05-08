package items.inventory;

import GUI.Observer;

public interface Observable {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers();

}
