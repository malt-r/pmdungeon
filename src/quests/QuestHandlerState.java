package quests;

import java.util.logging.Logger;

/** interface for state of the QuestHandler. */
public interface QuestHandlerState {
  Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * Will be called when entering the state.
   *
   * @param handler the QuestHandler of the state machine
   */
  default void enter(QuestHandler handler) {}

  /**
   * Will be called periodically in update method of questHandler.
   *
   * @param handler the QuestHandler of the state machine
   * @return the next state of the state machine or null, if the state won't change
   */
  default QuestHandlerState update(QuestHandler handler) {
    return null;
  }

  /**
   * Will be called when exiting the state
   *
   * @param handler the QuestHandler of the state machine
   */
  default void exit(QuestHandler handler) {}
}
