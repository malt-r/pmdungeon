package main;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import monsters.Monster;
import monsters.MonsterFactory;
import monsters.MonsterType;


import java.util.logging.Logger;

/**
 * The main game class.
 * <p>
 *     This class implements the MainController, which handles high level game logic, like the
 *     setup method and calling of the game loop.
 * </p>
 */
public class Game extends MainController {
    private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Hero hero;
    private DungeonWorld firstLevel;
    private final Monster[] monsterArray = new Monster[5];

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
        firstLevel = null;
        // the entityController will call hero.update each frame
        entityController.addEntity(hero);
        mainLogger.info("Hero created");

        for(int i=0;i<monsterArray.length;i++){
            MonsterType monsterType;
            if(i%2==0){
                monsterType = MonsterType.LIZARD;
            }
            else{
                monsterType = MonsterType.DEMON;
            }
            try{
                var mon = MonsterFactory.createMonster(monsterType,this);
                monsterArray[i]= mon;
                entityController.addEntity(mon);
                //TODO Add which kind of monster spawned
                mainLogger.info("Monster(" + (i + 1) + ") created");
            }
            catch(Exception e){
                mainLogger.severe(e.toString());
            }
        }
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
            mainLogger.info("Next stage loaded");
        }
        if (hero.isDead()) {
            try {
                levelController.loadDungeon(firstLevel);
            } catch (InvocationTargetException ex) {
                mainLogger.severe(ex.getMessage());
            } catch (IllegalAccessException ex) {
                mainLogger.severe(ex.getMessage());
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

        for (Monster monster : monsterArray) {
            monster.setLevel(levelController.getDungeon());
        }
    }

    /**
     * Returns all entities from the entityController.
     * This method is used by the combat system to enable ICombatable-instances to scan for attackable targets.
     * @return List of all entities in the game.
     */
    public ArrayList<IEntity> getAllEntities() {
        return entityController.getList();
    }
}
