package items.inventory;

import gui.OpenStateObserver;

/**
 * ObservableOpenState.
 */
public interface ObservableOpenState {
  void register(OpenStateObserver observer);

  void unregister(OpenStateObserver observer);

  void notifyObservers();
}
