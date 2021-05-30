package quests;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import main.Hero;

import java.util.ArrayList;
import java.util.logging.Logger;

/** Managerclass for the currently activated quest. */
public class QuestHandler implements IQuestObserver, IEntity {
  /** A logger. */
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private final Hero hero;
  private final ArrayList<IQuestObserver> questObservers;
  /** Current state of the statemachine */
  protected IQuestHandlerState currentState;
  /** Idle state of the statemachine */
  protected QuestHandlerIdleState idleState;

  private Quest currentQuest = null;
  private boolean removeCurrentQuest = false;
  /**
   * constructor
   *
   * @param hero the hero to manage quests for and apply rewards to.
   */
  public QuestHandler(Hero hero) {
    this.hero = hero;
    this.idleState = new QuestHandlerIdleState();
    this.currentState = this.idleState;
    this.questObservers = new ArrayList<>();
  }

  /**
   * Gets the idle state of the state machine
   *
   * @return
   */
  public IQuestHandlerState getIdleState() {
    return idleState;
  }

  /**
   * Getter of the currently activated quest.
   *
   * @return The currently activated quest.
   */
  public Quest getCurrentQuest() {
    return currentQuest;
  }

  /**
   * Register a questObserver.
   *
   * @param questObserver The observer to register.
   */
  public void register(IQuestObserver questObserver) {
    if (!questObservers.contains(questObserver)) {
      questObservers.add(questObserver);
    }
  }

  /**
   * unregister a questobserver.
   *
   * @param questObserver the observer to unregister.
   */
  public void unregister(IQuestObserver questObserver) {
    questObservers.remove(questObserver);
  }

  /** notify all registered questobservers about changes in currently activated quest. */
  private void notifyObservers() {
    for (IQuestObserver questObserver : questObservers) {
      questObserver.update(this.currentQuest);
    }
  }

  /**
   * Handle request for a new quest. Set statemachine to QuestHandlerPendingRequestState
   *
   * @param newQuest the new quest.
   * @param giver The questgiver, which started the request.
   */
  public void requestForNewQuest(Quest newQuest, QuestGiver giver) {
    mainLogger.info("accept quest? j/n");

    switchToState(new QuestHandlerPendingRequestState(newQuest, giver));
  }

  /**
   * Setup new quest. Cleans up old quest.
   *
   * @param quest
   */
  public void setupQuest(Quest quest) {
    mainLogger.info("Setting new current quest: " + quest.getQuestName());
    mainLogger.info("Description: " + quest.getDescription());
    mainLogger.info(quest.getRewardDescription());
    if (null != this.currentQuest) {
      this.currentQuest.unregister(this);
      this.currentQuest.cleanup();
    }
    this.currentQuest = quest;
    this.currentQuest.register(this);
    this.currentQuest.setup();

    notifyObservers();
  }

  /**
   * Has this handler a quest?
   *
   * @return True, if this handler has a quest.
   */
  public boolean hasQuest() {
    return null != currentQuest;
  }

  /** Abort pending request for new quest. */
  public void abortNewQuestRequest() {
    switchToState(this.idleState);
  }

  /**
   * {@inheritDoc}
   *
   * @param quest the quest, which sends the update
   */
  @Override
  public void update(Quest quest) {
    // current quest was updated
    if (quest.isFinished() && !removeCurrentQuest) {
      if (quest != this.currentQuest) {
        mainLogger.warning(
            "Quest from update "
                + quest
                + " does not match this.currentQuest "
                + this.currentQuest.toString());
      }
      mainLogger.info("Quest " + this.currentQuest.getQuestName() + " is finished");
      var reward = quest.getReward();
      currentQuest.cleanup();
      this.hero.applyReward(reward);
      removeCurrentQuest = true;
    } else {
      mainLogger.info(
          "Quest "
              + this.currentQuest.getQuestName()
              + " update: "
              + this.currentQuest.getProgressString());
    }

    notifyObservers();
  }

  /** {@inheritDoc} */
  @Override
  public void update() {
    // do not ever set the currentQuest = null in IQuestObserver.update -> may lead to
    // ConcurrentModificationException
    if (hasQuest() && this.removeCurrentQuest) {
      this.currentQuest.unregister(this);
      this.currentQuest = null;
      this.removeCurrentQuest = false;
    }

    var nextState = this.currentState.update(this);
    if (null != nextState) {
      switchToState(nextState);
    }
  }

  private void switchToState(IQuestHandlerState state) {
    this.currentState.exit(this);
    this.currentState = state;
    this.currentState.enter(this);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public boolean deleteable() {
    return false;
  }
}
