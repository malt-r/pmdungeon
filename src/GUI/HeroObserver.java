package GUI;

import main.Hero;

/**
 * Interface for the hero observer.
 */
public interface HeroObserver {
    /**
     * Updates the observer
     * @param hero hero instance which is provided for the update
     */
    public void update(Hero hero);
}
