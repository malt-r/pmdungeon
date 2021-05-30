package quests;

import GUI.InventoryObserver;
import items.Item;
import items.ItemFactory;
import items.inventory.Inventory;
import java.util.ArrayList;
import main.Hero;

/** Quest to collect random number of items. */
public class CollectItemsQuest extends Quest implements InventoryObserver {
  private final int toCollect;
  private final Hero hero;
  private int invCountLastUpdate;
  private int collected;

  /**
   * constructor.
   *
   * @param hero hero to observe the inventory of.
   */
  public CollectItemsQuest(Hero hero) {
    this.hero = hero;
    this.toCollect = util.math.Convenience.getRandBetween(3, 8);
    int xp = this.toCollect * 10;

    var items = ItemFactory.CreateRandomItems(1);
    this.reward = new QuestReward(items, xp);
  }


  /**
   * Constructor.
   *
   * @param hero The hero.
   * @param rewardItems The rewards.
   * @param toCollect Number of items to collect.
   */
  public CollectItemsQuest(Hero hero, ArrayList<Item> rewardItems, int toCollect) {
    this.hero = hero;
    this.toCollect = toCollect;
    int xp = this.toCollect * 10;

    this.reward = new QuestReward(rewardItems, xp);
  }

  public int getCollected() {
    return this.collected;
  }

  /**
   * {@inheritDoc}
   *
   * @return The Quest name.
   */
  @Override
  public String getQuestName() {
    return "Mein, dein, bürgerliche Kategorien.";
  }

  /**
   * {@inheritDoc}
   *
   * @return The progress string.
   */
  @Override
  public String getProgressString() {
    return "Sammle " + this.collected + " / " + this.toCollect + " Items.";
  }

  /**
   * {@inheritDoc}
   *
   * @return the description.
   */
  @Override
  public String getDescription() {
    return "Sammle " + toCollect + " Items";
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void setup() {
    hero.getInventory().register(this);
    this.invCountLastUpdate = hero.getInventory().getCount();
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void cleanup() {
    super.cleanup();
    hero.getInventory().unregister(this);
  }

  /**
   * {@inheritDoc}
   *
   * @return true if finished.
   */
  @Override
  public boolean isFinished() {
    return this.collected >= this.toCollect;
  }

  /**
   * {@inheritDoc}
   *
   */
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
