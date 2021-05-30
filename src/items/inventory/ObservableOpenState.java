package items.inventory;

import gui.OpenStateObserver;

public interface ObservableOpenState {
  void register(OpenStateObserver observer);

  void unregister(OpenStateObserver observer);

  void notifyObservers();
}
