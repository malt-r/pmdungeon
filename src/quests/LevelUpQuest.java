package quests;

import GUI.HeroObserver;
import items.ItemFactory;
import main.Hero;

/**
 * Quest to level up random amount of levels.
 */
public class LevelUpQuest extends Quest implements HeroObserver {
    private int startLevel;
    private int levels;
    private boolean isFinished;
    private final Hero hero;

    /**
     * constructor
     * @param hero the hero to observer for level ups
     */
    public LevelUpQuest(Hero hero) {
        this.hero = hero;
        int xp = 120;

        this.levels = util.math.Convenience.getRandBetween(2, 4);

        var items = ItemFactory.CreateRandomItems(1);
        this.reward = new QuestReward(items, xp);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getQuestName() {
        return "Level go brrr";
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getProgressString() {
        return "Level up " + (this.hero.getLevel().getCurrentLevel() - this.startLevel) + " / " + this.levels + " levels";
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getDescription() {
        return "Levels to level up: " + this.levels;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public void setup() {
        this.hero.register(this);
        this.startLevel = this.hero.getLevel().getCurrentLevel();
        this.isFinished = false;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public void cleanup() {
        super.cleanup();
        this.hero.unregister(this);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * {@inheritDoc}
     * @return
     */
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
