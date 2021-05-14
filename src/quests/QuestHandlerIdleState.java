package quests;

public class QuestHandlerIdleState implements IQuestHandlerState {
    @Override
    public void enter(QuestHandler handler) {
        mainLogger.info("Enter idle quest handler state");
    }
}
