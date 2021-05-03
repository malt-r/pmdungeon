package main;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.Chest;
import items.Item;
import items.ItemFactory;
import items.ItemType;
import main.sample.DebugControl;
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
    private static Game instance;
    private Hero hero;
    private DungeonWorld firstLevel;
    private ArrayList <IEntity> entitiesToRemove = new ArrayList<>();
    private int currentLevelIndex =0;
    public static Game getInstance(){
        if(Game.instance==null){
            Game.instance = new Game();
        }
        return Game.instance;
    }
    /**
     * Setup of the game world.
     * <p>
     *     This will create the playable instance of the Hero and cache
     *     the first level as a fallback for game over (if the Hero dies).
     * </p>
     */
    @Override
    protected void setup() {
        hero = new Hero();
        firstLevel = null;
        // the entityController will call hero.update each frame
        entityController.addEntity(hero);
        mainLogger.info("Hero created");
        // attach camera to hero
        camera.follow(hero);


        // test
        //BaseBag bag = new BaseBag(this);
        //BaseBag weaponBag = new WeaponBag(this);

        //var sword = new RegularSword(this);
        //Item sword2 = new RegularSword(this);
        //Item potion = new HealthPotion(this);
        //// this is pretty unsafe code and should be wrapped in try catch
        //weaponBag.addItem(sword);
        //weaponBag.addItem(sword2);
        //var can = weaponBag.canAddItem(sword2);
        //if (can) {
        //    weaponBag.addItem(sword2);
        //}
        //can = weaponBag.canAddItem(potion);
        //if (can) {
        //    weaponBag.addItem(potion);
        //}

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
            currentLevelIndex++;
            entityController.removeAllFrom(Item.class);
            entityController.removeAllFrom(Monster.class);
            levelController.triggerNextStage();
            mainLogger.info("Next stage loaded");

        }

        if (hero.isDead()) {
            try {
                levelController.loadDungeon(firstLevel);
                currentLevelIndex =0;
            } catch (InvocationTargetException ex) {
                mainLogger.severe(ex.getMessage());
            } catch (IllegalAccessException ex) {
                mainLogger.severe(ex.getMessage());
            }
        }

        for(IEntity entity : entitiesToRemove){
            this.entityController.removeEntity(entity);
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

        //test_SpawnAllItemsAndMonster();

        var levelInfo = new LevelInfo();
        var content = levelInfo.getLevelContent(currentLevelIndex);

        try {
            Spawner.spawnEntities(content,levelController,entityController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all entities from the entityController.
     * This method is used by the combat system to enable ICombatable-instances to scan for attackable targets.
     * @return List of all entities in the game.
     */
    public ArrayList<IEntity> getAllEntities() {
        return this.entityController.getList();
    }

    /*private void handleItemPicking(){
        var allEntities = getAllEntities();
            for (IEntity entity : allEntities) {
                if (entity instanceof Item) {
                    var item = (Item) entity;
                    if(checkForTrigger(item.getPosition())){
                        //TODO: Add to inventory
                        System.out.println(item.getName());
                    }
                }
            }

    }*/


    public boolean checkForTrigger(Point p) {
        //return (int)p.x == (int) this.hero.position.x && (int)p.y == (int)this.hero.position.y;
        var level = levelController.getDungeon();
        int ownX = Math.round(hero.position.x);
        int ownY = Math.round(hero.position.y);
        var ownTile = level.getTileAt(ownX, ownY);

        int otherX = Math.round(p.x);
        int otherY = Math.round(p.y);
        var otherTile = level.getTileAt(otherX, otherY);

        return ownTile == otherTile;
    }

    public void addEntity(IEntity entity){
        this.entityController.addEntity(entity);
    }

    public void deleteEntity(IEntity entity){
        this.entitiesToRemove.add(entity);
    }

    public void test_SpawnAllItemsAndMonster(){
        DebugControl.SpawnAll(entityController,levelController);
    }
}
