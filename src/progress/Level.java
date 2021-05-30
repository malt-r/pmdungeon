package progress;

import gui.LevelObserver;
import java.util.ArrayList;
import main.Actor;
import progress.ability.Ability;



/**
 * Encapsulates basic calculations for progress. Calculates the amount of xp needed to reach the
 * next level (currently just a first test).
 *
 * @author malte
 */
public class Level implements ObserveableLevel {
  private final float xpConstant = 5.20f; //
  private final float healthScaleConstant = 3.f;
  private final float damageScaleConstant = 2.f;
  private final ArrayList<LevelObserver> observerList = new ArrayList<LevelObserver>();
  private final ArrayList<Ability> abilities = new ArrayList<>();
  private final Runnable levelUpCallback;
  private int level;
  private int xp;

  /** constructor. */
  public Level(Runnable levelUpCallback) {
    this.level = 1;
    this.levelUpCallback = levelUpCallback;
  }

  /**
   * addAbility.
   *
   * @param ability ability
   * @return if sucessfull
   */
  public boolean addAbility(Ability ability) {
    for (var ab : abilities) {
      if (ab.getClass() == ability.getClass()) {
        return false;
      }
    }
    abilities.add(ability);
    return true;
  }

  /**
   * checkForAbilityActivation.
   *
   * @param origin origin
   */
  public void checkForAbilityActivation(Actor origin) {
    for (Ability ability : abilities) {
      ability.checkForActivation(origin);
    }
  }

  /**
   * resets the level.
   */
  public void reset() {
    this.abilities.clear();
    this.xp = 0;
    this.level = 1;
    notifyObservers();
  }

  /**
   * Get current xp.
   *
   * @return current xp.
   */
  public int getCurrentXp() {
    return xp;
  }

  /**
   * Add amount of xp to the current xp counter.
   *
   * @param amount The amount of xp to add
   * @return True, if a level up occured (temporary solution and this will likely change).
   */
  public boolean increaseXp(int amount) {
    this.xp += amount;
    // level up
    int xpForNextLevel = getXpForNextLevelTotal();
    if (this.xp >= xpForNextLevel) {
      this.xp -= xpForNextLevel;
      this.level += 1;
      this.levelUpCallback.run();
      notifyObservers();
      return true;
    } else {
      notifyObservers();
      return false;
    }
  }

  /**
   * Gets the increment of health, which should be applied to the health of the hero.
   *
   * @return Currently a constant value. Dependent on the implementation of the handling of
   *     modifiers of stats, this will change.
   */
  public float getHealthIncrementForCurrentLevel() {
    return this.healthScaleConstant;
  }

  /**
   * Gets the increment of damage, which should be applied to the base damage of the hero.
   *
   * @return Currently a constant value. Dependent on the implementation of the handling of
   *     modifiers of stats, this will change.
   */
  public float getDamageIncrementForCurrentLevel() {
    return this.damageScaleConstant;
  }

  /**
   * Calculates the amount of xp left to reach the new level.
   *
   * @return nextlevel left
   */
  public int getXpForNextLevelLeft() {
    int total = getXpForNextLevelTotal();
    return total - this.xp;
  }

  /**
   * Calculates the total amount of xp to acquire to reach the new level.
   *
   * @return nextleveltotal
   */
  public int getXpForNextLevelTotal() {
    return (int) Math.pow(((double) this.level * (double) this.xpConstant), 2);
  }

  /**
   * Gets the current level.
   *
   * @return The current level.
   */
  public int getCurrentLevel() {
    return level;
  }

  /**
   * Registers an observer.
   *
   * @param observer to be registered.
   */
  @Override
  public void register(LevelObserver observer) {
    this.observerList.add(observer);
  }

  /**
   * Unregisters an observer.
   *
   * @param observer to be unregistered.
   */
  @Override
  public void unregister(LevelObserver observer) {
    this.observerList.remove(observer);
  }

  /** notifies all observers. */
  @Override
  public void notifyObservers() {
    int numObservers = observerList.size();
    for (int i = 0; i < observerList.size(); i++) {
      observerList.get(i).update(this);
      if (observerList.size() < numObservers) {
        i--;
        numObservers = this.observerList.size();
      }
    }
  }
}
