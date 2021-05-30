package quests;

/** Observable interface for a quest. */
public interface ObservableQuest {
  /**
   * register new observer.
   *
   * @param observer the observer to register.
   */
  void register(QuestObserver observer);

  /**
   * unregister registered observer.
   *
   * @param observer the observer to unregister.
   */
  void unregister(QuestObserver observer);

  /** notify observers. */
  void notifyObservers();
}
