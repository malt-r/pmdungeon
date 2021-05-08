package items.inventory;


import GUI.OpenStateObserver;

public interface ObservableOpenState {
    void register(OpenStateObserver observer);
    void unregister(OpenStateObserver observer);
    void notifyObservers();
}
