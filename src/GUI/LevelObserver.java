package GUI;

import progress.Level;

/**
 * interface for the level observer
 */
public interface LevelObserver {
    /**
     * updates a level observer with a current level.
     * @param level current level
     */
    void update(Level level);
}
