package main;

import gui.HeroObserver;

public interface ObservableHero {
  void register(HeroObserver observer);

  void unregister(HeroObserver observer);

  void notifyObservers();
}
