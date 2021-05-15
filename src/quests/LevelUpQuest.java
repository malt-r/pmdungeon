package quests;

import GUI.HeroObserver;
import items.ItemFactory;
import main.Hero;

public class LevelUpQuest extends Quest implements HeroObserver {
    private int startLevel;
    private int levels;
    private boolean isFinished;
    private final Hero hero;

    public LevelUpQuest(Hero hero) {
        this.hero = hero;
        int xp = 120;

        this.levels = util.math.Convenience.getRandBetween(2, 4);

        var items = ItemFactory.CreateRandomItems(1);
        this.reward = new QuestReward(items, xp);
    }

    @Override
    public String getQuestName() {
        return "Level go brrr";
    }

    @Override
    public String getProgressString() {
        return "Level up " + (this.hero.getLevel().getCurrentLevel() - this.startLevel) + " / " + this.levels + " levels";
    }

    @Override
    public String getDescription() {
        return "Levels to level up: " + this.levels;
    }

    @Override
    public void setup() {
        this.hero.register(this);
        this.startLevel = this.hero.getLevel().getCurrentLevel();
        this.isFinished = false;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        this.hero.unregister(this);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void update(Hero hero) {
        mainLogger.info("startLevel: " + this.startLevel);
        mainLogger.info("levels: " + this.levels);

        if (hero.getLevel().getCurrentLevel() >= this.startLevel + this.levels) {
            this.isFinished = true;
        }
        notifyObservers();
    }
}
