package progress;

import GUI.LevelObserver;

public interface ObserveableLevel {
    void register(LevelObserver observer);
    void unregister(LevelObserver observer);
    void notifyObservers();
}
