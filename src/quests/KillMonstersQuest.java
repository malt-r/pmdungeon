package quests;

import GUI.HeroObserver;
import items.Item;
import items.ItemFactory;
import main.Hero;

import java.util.ArrayList;

/**
 * Quest to kill random amount of monsters
 */
public class KillMonstersQuest extends Quest implements HeroObserver {
    private final int toKill;
    private int killed;
    private int startKillCount;
    private final Hero hero;

    public int getKilled() {
        return killed;
    }
    /**
     * constructor.
     * @param hero the hero which to observer for killed monsters
     */
    public KillMonstersQuest(Hero hero) {
        this.hero = hero;
        this.toKill = util.math.Convenience.getRandBetween(3, 8);
        int xp = this.toKill * 30;

        var items = ItemFactory.CreateRandomItems(2);
        this.reward = new QuestReward(items, xp);
    }

    public KillMonstersQuest(Hero hero, ArrayList<Item> rewardItems) {
        this.hero = hero;
        this.toKill = util.math.Convenience.getRandBetween(3, 8);
        int xp = this.toKill * 30;

        //var items = ItemFactory.CreateRandomItems(2);
        this.reward = new QuestReward(rewardItems, xp);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getQuestName() {
        return "Another one bites the dust";
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getProgressString() {
        return "Killed " + killed + " / " + toKill + " monsters";
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getDescription() {
        return "Kill " + toKill + " monsters.";
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public void setup() {
        this.hero.register(this);
        this.startKillCount = this.hero.getKillCount();
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
        return this.killed >= this.toKill;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public void update(Hero hero) {
        this.killed = hero.getKillCount() - this.startKillCount;
        mainLogger.info("update! killed: " + this.killed);
        notifyObservers();
    }
}
