package items.inventory;

import GUI.HeroObserver;
import main.Hero;

public interface ObservableHero extends Observable {
    void register(HeroObserver observer);
    void unregister(Hero observer);
    void notifyObservers();
}
