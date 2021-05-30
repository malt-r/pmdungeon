package quests;

import gui.HeroObserver;
import items.Item;
import items.ItemFactory;
import java.util.ArrayList;
import main.Hero;


/** Quest to kill random amount of monsters. */
public class KillMonstersQuest extends Quest implements HeroObserver {
  private final int toKill;
  private final Hero hero;
  private int killed;
  private int startKillCount;

  /**
   * constructor.
   *
   * @param hero the hero which to observer for killed monsters
   */
  public KillMonstersQuest(Hero hero) {
    this.hero = hero;
    this.toKill = util.math.Convenience.getRandBetween(3, 8);
    int xp = this.toKill * 30;

    var items = ItemFactory.CreateRandomItems(2);
    this.reward = new QuestReward(items, xp);
  }

  /**
   * KillMonstersQuest.
   *
   *
   * @param hero hero
   *
   * @param rewardItems rewardItems
   */
  public KillMonstersQuest(Hero hero, ArrayList<Item> rewardItems) {
    this.hero = hero;
    this.toKill = util.math.Convenience.getRandBetween(3, 8);
    int xp = this.toKill * 30;

    // var items = ItemFactory.CreateRandomItems(2);
    this.reward = new QuestReward(rewardItems, xp);
  }

  public int getKilled() {
    return killed;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String getQuestName() {
    return "Another one bites the dust";
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String getProgressString() {
    return "Killed " + killed + " / " + toKill + " monsters";
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String getDescription() {
    return "Kill " + toKill + " monsters.";
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void setup() {
    this.hero.register(this);
    this.startKillCount = this.hero.getKillCount();
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void cleanup() {
    super.cleanup();
    this.hero.unregister(this);
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public boolean isFinished() {
    return this.killed >= this.toKill;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void update(Hero hero) {
    this.killed = hero.getKillCount() - this.startKillCount;
    mainLogger.info("update! killed: " + this.killed);
    notifyObservers();
  }
}
