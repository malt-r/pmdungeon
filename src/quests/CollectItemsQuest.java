package quests;

import GUI.HeroObserver;
import items.ItemFactory;
import main.Hero;

public class CollectItemsQuest extends Quest implements HeroObserver {
    private int invCountLastUpdate;
    private int toCollect;
    private int collected;
    private Hero hero;

    public CollectItemsQuest(Hero hero) {
        this.hero = hero;
        this.toCollect = util.math.Convenience.getRandBetween(3, 8);
        int xp = this.toCollect * 10;

        var items = ItemFactory.CreateRandomItems(1);
        this.reward = new QuestReward(items, xp);
    }

    @Override
    public String getQuestName() {
        return "Mein, dein, bürgerliche Kategorien.";
    }

    @Override
    public String getProgressString() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Sammle " + toCollect + " Items";
    }

    @Override
    public void setup() {
        hero.register(this);
    }

    @Override
    public void cleanup() {
        hero.unregister(this);

    }

    @Override
    public boolean isFinished() {
        return this.collected >= this.toCollect;
    }

    @Override
    public void update(Hero hero) {
        int inventoryCount = hero.getInventory().getCount();
        if (this.invCountLastUpdate < inventoryCount) {
            collected += inventoryCount - this.invCountLastUpdate;
        }

        this.invCountLastUpdate = inventoryCount;
    }
}
