package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import main.Game;
import main.Hero;

import java.util.ArrayList;
import java.util.logging.Logger;

public class QuestHandler implements IQuestObserver {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Quest currentQuest = null;
    private Hero hero;
    private ArrayList<IQuestObserver> questObservers;

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
    }

    public boolean acceptNewQuest(Quest newQuest) {
        mainLogger.info("accept quest? j/n");
        // TODO:
        //Game.getInstance().getQuestDialog().show(newQuest, this.currentQuest);

        boolean choice = false, accept = false;
        do {
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                accept = true;
                choice = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                choice = true;
            }

        } while(!choice);

        if (accept) {
            setupQuest(newQuest);
        }
        return accept;
    }

    private void setupQuest(Quest quest) {
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
    }

    public boolean hasQuest() {
        return null != currentQuest;
    }

    @Override
    public void update(Quest quest) {
        // current quest was updated
        if (quest.isFinished()) {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " is finished");
            var reward = quest.getReward();
            this.hero.applyReward(reward);
            currentQuest.cleanup();
            currentQuest = null;
        } else {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " update: " + this.currentQuest.getProgressString());
        }

        notifyObservers();
    }
}
