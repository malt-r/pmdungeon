package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import main.Hero;

import java.util.ArrayList;
import java.util.logging.Logger;

public class QuestHandler implements IQuestObserver, IEntity {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected IQuestHandlerState currentState;
    protected QuestHandlerIdleState idleState;
    public IQuestHandlerState getIdleState(){
        return idleState;
    }

    private Quest currentQuest = null;
    private boolean removeCurrentQuest = false;
    private Hero hero;
    private ArrayList<IQuestObserver> questObservers;

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void register(IQuestObserver questObserver) {
        if (!questObservers.contains(questObserver)) {
            questObservers.add(questObserver);
        }
    }

    public void unregister(IQuestObserver questObserver) {
        if (questObservers.contains(questObserver)) {
            questObservers.remove(questObserver);
        }
    }

    private void notifyObservers() {
        for (IQuestObserver questObserver : questObservers) {
            questObserver.update(this.currentQuest);
        }
    }

    public QuestHandler(Hero hero) {
        this.hero = hero;
        this.idleState = new QuestHandlerIdleState();
        this.currentState = this.idleState;
        this.questObservers = new ArrayList<>();
    }

    public void requestForNewQuest(Quest newQuest, QuestGiver giver) {
        mainLogger.info("accept quest? j/n");
        // TODO:
        //Game.getInstance().getQuestDialog().show(newQuest, this.currentQuest);

        switchToState(new QuestHandlerPendingRequestState(newQuest, giver));
    }

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

    public boolean hasQuest() {
        return null != currentQuest;
    }

    public void abortNewQuestRequest() {
        switchToState(this.idleState);
    }

    // IQuestObserver.update
    @Override
    public void update(Quest quest) {
        // current quest was updated
        if (quest.isFinished() && !removeCurrentQuest) {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " is finished");
            var reward = quest.getReward();
            currentQuest.cleanup();
            this.hero.applyReward(reward);
            removeCurrentQuest = true;
        } else {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " update: " + this.currentQuest.getProgressString());
        }

        notifyObservers();
    }


    // IEntity.update
    @Override
    public void update() {
        // do not ever set the currentQuest = null in IQuestObserver.update -> may lead to ConcurrentModificationException
        if (hasQuest() && this.removeCurrentQuest) {
            currentQuest = null;
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

    @Override
    public boolean deleteable() {
        return false;
    }
}
