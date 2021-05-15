package quests;

/**
 * Observable interface for a quest
 */
public interface IObservableQuest {
    /**
     * register new observer
     * @param observer the observer to register
     */
    void register(IQuestObserver observer);

    /**
     * unregister registered observer
     * @param observer the observer to unregister
     */
    void unregister(IQuestObserver observer);

    /**
     * notify observers
     */
    void notifyObservers();
}
