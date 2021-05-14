package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class QuestHandlerPendingRequestState implements IQuestHandlerState{

    private QuestGiver giver;
    private boolean receivedInput;

    public QuestHandlerPendingRequestState(QuestGiver giver) {
        this.giver = giver;
        this.receivedInput = false;
    }

    @Override
    public void enter(QuestHandler handler) {
        // call HUD
    }

    @Override
    public IQuestHandlerState update(QuestHandler handler) {
        IQuestHandlerState nextState = null;
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            giver.acceptedQuest(true);
            this.receivedInput = true;
            nextState = handler.getIdleState();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            giver.acceptedQuest(false);
            this.receivedInput = true;
            nextState = handler.getIdleState();
        }
        return nextState;
    }

    @Override
    public void exit(QuestHandler handler) {
        if (!this.receivedInput) {
            giver.acceptedQuest(false);
        }
    }
}
