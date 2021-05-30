package quests;

import java.util.ArrayList;
import java.util.logging.Logger;

/** Abstract base class of quests. */
public abstract class Quest implements ObservableQuest {
  /** A logger. */
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private final ArrayList<QuestObserver> observers;

  /** The reward to apply to a hero, if the quest goal is fulfilled. */
  protected QuestReward reward;

  /**
   * Flag to check, if the reward can be retrieved from this quest -> will be set to false in
   * cleanup.
   */
  protected boolean rewardIsAvailable = true;

  /** constructor. */
  public Quest() {
    observers = new ArrayList<>();
  }

  /**
   * Returns the questname.
   *
   * @return the questname.
   */
  public abstract String getQuestName();

  /**
   * Returns the current progress of the quest.
   *
   * @return a description of the current progress of the quest.
   */
  public abstract String getProgressString();

  /**
   * Returns a description of the quest.
   *
   * @return the description of the quest.
   */
  public abstract String getDescription();

  /**
   * Returns a description of the rewards of this quest.
   *
   * @return the reward description.
   */
  public String getRewardDescription() {
    return null != this.reward ? this.reward.getRewardDescription() : "";
  }

  /**
   * Initial setup of the quest (this is the place to register the quest as an observer where
   * necessary).
   */
  public abstract void setup();

  /** Cleanup function (this is the place to
   * unregister the quest as an observer where necessary). */
  public void cleanup() {
    this.rewardIsAvailable = false;
  }

  /**
   * Is the quest-goal fulfilled.
   *
   * @return if finished
   */
  public abstract boolean isFinished();

  /** {@inheritDoc} */
  @Override
  public void notifyObservers() {
    for (QuestObserver observer : observers) {
      observer.update(this);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void register(QuestObserver observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void unregister(QuestObserver observer) {
    observers.remove(observer);
  }

  /**
   * Returns the reward of this quest.
   *
   * @return the reward of this quest.
   */
  public QuestReward getReward() {
    if (this.rewardIsAvailable) {
      return reward;
    }
    return null;
  }
}
