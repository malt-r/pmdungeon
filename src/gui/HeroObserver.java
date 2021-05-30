package gui;

import main.Hero;

/** Interface for the hero observer. */
public interface HeroObserver {
  /**
   * Updates the observer.
   *
   * @param hero hero instance which is provided for the update
   */
  void update(Hero hero);
}
