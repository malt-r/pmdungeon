package quests;

/** Observer of quests. */
public interface QuestObserver {
  /**
   * Will be called by the observable Quest, on which this observer is registered.
   *
   * @param quest the quest, which sends the update.
   */
  void update(Quest quest);
}
