package main;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import main.sample.MockMonster;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


/**
 * The main game class.
 * <p>
 *     This class implements the MainController, which handles high level game logic, like the
 *     setup method and calling of the game loop.
 * </p>
 */
public class Game extends MainController {
    private Hero hero;
    private MockMonster monster;
    private DungeonWorld firstLevel;

    /**
     * Setup of the game world.
     * <p>
     *     This will create the playable instance of the Hero and cache
     *     the first level as a fallback for game over (if the Hero dies).
     * </p>
     */
    @Override
    protected void setup() {
        hero = new Hero(this);
        monster = new MockMonster(this);

        // the entityController will call hero.update each frame
        entityController.addEntity(hero);
        entityController.addEntity(monster);
        firstLevel = null;

        // attach camera to hero
        camera.follow(hero);
    }

    /**
     * Implements logic executed at the begin of a frame.
     */
    @Override
    protected void beginFrame() {

    }

    /**
     * Implements logic executed at the end of a frame.
     * <p>
     *     This will check, if the Hero finished the current level and needs to be spawned in a new one.
     *     If the hero is dead, this method will reload the first level.
     * </p>
     */
    @Override
    protected void endFrame() {
        // check, if current position of hero is on the trigger to load a new level
        if (levelController.checkForTrigger(hero.getPosition()) ) {
            levelController.triggerNextStage();
        }
        if (hero.isDead()) {
            try {
                levelController.loadDungeon(firstLevel);
            } catch (InvocationTargetException ex) {
            } catch (IllegalAccessException ex) {
            }
        }
    }

    /**
     * Implements logic executed on load of a new level.
     * If the internal cache of the first level was not previously set, it will be set in this method.
     */
    @Override
    public void  onLevelLoad() {
        // cache the first level to be able to spawn hero back in after game over
        if (null == firstLevel) {
            firstLevel = levelController.getDungeon();
        }

        // set the level of the hero
        hero.setLevel(levelController.getDungeon());
        monster.setLevel(levelController.getDungeon());
    }

    /**
     * Returns all entities from the entityController.
     * This method is used by the combat system to enable ICombatable-instances to scan for attackable targets.
     * @return
     */
    public ArrayList<IEntity> getAllEntities() {
        return entityController.getList();
    }
}
