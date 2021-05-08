package main;

import GUI.HeroObserver;
import main.Hero;

public interface ObservableHero {
    void register(HeroObserver observer);
    void unregister(HeroObserver observer);
    void notifyObservers();
}
