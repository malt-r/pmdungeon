package main;

import gui.HeroObserver;

/**
 * ObservableHero
 */
public interface ObservableHero {
  void register(HeroObserver observer);

  void unregister(HeroObserver observer);

  void notifyObservers();
}
