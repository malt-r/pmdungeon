package quests;

public interface IQuestHandlerState {
    default void enter(QuestHandler handler){}
    default IQuestHandlerState update(QuestHandler handler){return null;}
    default void exit(QuestHandler handler){}
}
