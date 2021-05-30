package quests;

import GUI.LevelObserver;
import items.Item;
import items.ItemFactory;
import main.Hero;
import progress.Level;

import java.util.ArrayList;

/** Quest to level up random amount of levels. */
public class LevelUpQuest extends Quest implements LevelObserver {
  private final int levels;
  private final Hero hero;
  private int startLevel;
  private boolean isFinished;

  /**
   * constructor
   *
   * @param hero the hero to observer for level ups
   */
  public LevelUpQuest(Hero hero) {
    this.hero = hero;
    int xp = 120;

    this.levels = util.math.Convenience.getRandBetween(2, 4);

    var items = ItemFactory.CreateRandomItems(1);
    this.reward = new QuestReward(items, xp);
  }

  public LevelUpQuest(Hero hero, ArrayList<Item> rewardItems, int levelsToLevel) {
    this.hero = hero;
    int xp = 120;

    this.levels = levelsToLevel;
    this.reward = new QuestReward(rewardItems, xp);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public String getQuestName() {
    return "Level go brrr";
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
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
   * @return
   */
  @Override
  public String getDescription() {
    return "Levels to level up: " + this.levels;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public void setup() {
    this.hero.getLevel().register(this);
    this.startLevel = this.hero.getLevel().getCurrentLevel();
    this.isFinished = false;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public void cleanup() {
    super.cleanup();
    this.hero.getLevel().unregister(this);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
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
