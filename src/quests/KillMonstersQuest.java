package quests;

import GUI.HeroObserver;
import items.ItemFactory;
import main.Hero;

public class KillMonstersQuest extends Quest implements HeroObserver {
    private final int toKill;
    private int killed;
    private int startKillCount;
    private final Hero hero;

    public KillMonstersQuest(Hero hero) {
        this.hero = hero;
        this.toKill = util.math.Convenience.getRandBetween(3, 8);
        int xp = this.toKill * 30;

        var items = ItemFactory.CreateRandomItems(2);
        this.reward = new QuestReward(items, xp);
    }

    @Override
    public String getQuestName() {
        return "Another one bites the dust";
    }

    @Override
    public String getProgressString() {
        return "Killed " + killed + " / " + toKill + " monsters";
    }

    @Override
    public String getDescription() {
        return "Kill " + toKill + " monsters.";
    }

    @Override
    public void setup() {
        this.hero.register(this);
        this.startKillCount = this.hero.getKillCount();
    }

    @Override
    public void cleanup() {
        this.hero.unregister(this);
    }

    @Override
    public boolean isFinished() {
        return this.killed >= this.toKill;
    }

    @Override
    public void update(Hero hero) {
        this.killed = hero.getKillCount() - this.startKillCount;
        mainLogger.info("update! killed: " + this.killed);
        notifyObservers();
    }
}
