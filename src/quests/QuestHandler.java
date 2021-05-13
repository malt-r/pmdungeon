package quests;

import main.Hero;

import java.util.logging.Logger;

public class QuestHandler implements IQuestObserver {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Quest currentQuest = null;
    private Hero hero;

    public QuestHandler(Hero hero) {
        this.hero = hero;
    }

    public void requestForNewQuest(Quest newQuest) {
        boolean overrideQuest = true;
        if (hasQuest()) {
            // TODO: call hud and query for override of current quest
        }
        if (overrideQuest) {
            setupQuest(newQuest);
        }
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
        // TODO: update hud, if status of quest changed
        if (quest.isFinished()) {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " is finished");
            var reward = quest.getReward();
            this.hero.applyReward(reward);
            currentQuest.cleanup();
            currentQuest = null;
        } else {
            mainLogger.info("Quest " + this.currentQuest.getQuestName() + " update: " + this.currentQuest.getProgressString());
        }
    }
}
