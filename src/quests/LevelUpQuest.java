package quests;

import gui.LevelObserver;
import items.Item;
import items.ItemFactory;
import java.util.ArrayList;
import main.Hero;
import progress.Level;



/** Quest to level up random amount of levels. */
public class LevelUpQuest extends Quest implements LevelObserver {
  private final int levels;
  private final Hero hero;
  private int startLevel;
  private boolean isFinished;

  /**
   * constructor.
   *
   * @param hero the hero to observer for level ups.
   */
  public LevelUpQuest(Hero hero) {
    this.hero = hero;
    int xp = 120;

    this.levels = util.math.Convenience.getRandBetween(2, 4);

    var items = ItemFactory.createRandomItems(1);
    this.reward = new QuestReward(items, xp);
  }

  /**
   * LevelUpQuest.
   *
   * @param hero hero
   * @param rewardItems rewardItems
   * @param levelsToLevel levelsToLevel
   */
  public LevelUpQuest(Hero hero, ArrayList<Item> rewardItems, int levelsToLevel) {
    this.hero = hero;
    int xp = 120;

    this.levels = levelsToLevel;
    this.reward = new QuestReward(rewardItems, xp);
  }

  /** {@inheritDoc} */
  @Override
  public String getQuestName() {
    return "Level go brrr";
  }

  /** {@inheritDoc} */
  @Override
  public String getProgressString() {
    return "Level up "
        + (this.hero.getLevel().getCurrentLevel() - this.startLevel)
        + " / "
        + this.levels
        + " levels";
  }

  /**
   * {@inheritDoc}
   *
   * @return description
   */
  @Override
  public String getDescription() {
    return "Levels to level up: " + this.levels;
  }

  /** {@inheritDoc} */
  @Override
  public void setup() {
    this.hero.getLevel().register(this);
    this.startLevel = this.hero.getLevel().getCurrentLevel();
    this.isFinished = false;
  }

  /** {@inheritDoc} */
  @Override
  public void cleanup() {
    super.cleanup();
    this.hero.getLevel().unregister(this);
  }

  /**
   * {@inheritDoc}
   *
   * @return if finishes
   */
  @Override
  public boolean isFinished() {
    return isFinished;
  }

  /** {@inheritDoc} */
  @Override
  public void update(Level level) {
    mainLogger.info("startLevel: " + this.startLevel);
    mainLogger.info("levels: " + this.levels);

    if (level.getCurrentLevel() >= this.startLevel + this.levels) {
      this.isFinished = true;
    }
    notifyObservers();
  }
}
