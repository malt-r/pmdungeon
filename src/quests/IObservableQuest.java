package quests;

public interface IObservableQuest {
    void register(IQuestObserver observer);
    void unregister(IQuestObserver observer);
    void notifyObservers();
}
