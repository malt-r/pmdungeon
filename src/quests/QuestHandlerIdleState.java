package quests;

/** Idle state of the state machine of a quest handler. */
public class QuestHandlerIdleState implements IQuestHandlerState {

  /**
   * {@inheritDoc}
   *
   * @param handler the QuestHandler of the state machine
   */
  @Override
  public void enter(QuestHandler handler) {
    mainLogger.info("Enter idle quest handler state");
  }
}
