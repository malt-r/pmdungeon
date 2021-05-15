package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class QuestHandlerPendingRequestState implements IQuestHandlerState{

    private QuestGiver giver;
    private Quest pendingQuest;
    private boolean receivedInput;

    public QuestHandlerPendingRequestState(Quest pendingQuest, QuestGiver giver) {
        this.giver = giver;
        this.receivedInput = false;
        this.pendingQuest = pendingQuest;
    }

    @Override
    public void enter(QuestHandler handler) {
        // call HUD
        mainLogger.info("Enter pending request");
        main.Game.getInstance().getQuestDialog().show(pendingQuest, handler.getCurrentQuest());
    }

    @Override
    public IQuestHandlerState update(QuestHandler handler) {
        IQuestHandlerState nextState = null;
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            giver.questWasAccepted(true);
            this.receivedInput = true;
            handler.setupQuest(pendingQuest);
            nextState = handler.getIdleState();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            giver.questWasAccepted(false);
            this.receivedInput = true;
            nextState = handler.getIdleState();
        }
        return nextState;
    }

    @Override
    public void exit(QuestHandler handler) {
        if (!this.receivedInput) {
            giver.questWasAccepted(false);
        }
        main.Game.getInstance().getQuestDialog().hide();
    }
}
