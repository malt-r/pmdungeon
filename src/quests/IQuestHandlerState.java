package quests;

import java.util.logging.Logger;

public interface IQuestHandlerState {
    Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    default void enter(QuestHandler handler){}
    default IQuestHandlerState update(QuestHandler handler){return null;}
    default void exit(QuestHandler handler){}
}
