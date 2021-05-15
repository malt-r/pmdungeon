package quests;

import GUI.HeroObserver;
import GUI.InventoryObserver;
import GUI.LevelObserver;
import items.ItemFactory;
import items.inventory.Inventory;
import main.Hero;
import progress.Level;

public class CollectItemsQuest extends Quest implements InventoryObserver {
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
        return "Sammle " + this.collected + " / " + this.toCollect + " Items.";
    }

    @Override
    public String getDescription() {
        return "Sammle " + toCollect + " Items";
    }

    @Override
    public void setup() {
        hero.getInventory().register(this);
        this.invCountLastUpdate = hero.getInventory().getCount();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        hero.getInventory().unregister(this);
    }

    @Override
    public boolean isFinished() {
        return this.collected >= this.toCollect;
    }

    @Override
    public void update(Inventory inv, boolean fromHero) {
        if (fromHero) {
            int inventoryCount = inv.getCount();
            if (this.invCountLastUpdate < inventoryCount) {
                collected += inventoryCount - this.invCountLastUpdate;
            }

            this.invCountLastUpdate = inventoryCount;

            notifyObservers();
        }
    }
}
