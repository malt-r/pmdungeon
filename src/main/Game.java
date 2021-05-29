package main;

import GUI.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.Item;
import items.chests.Chest;
import main.sample.DebugControl;
import monsters.Monster;
import monsters.MonsterType;
import quests.QuestGiver;
import quests.QuestHandler;
import traps.*;
import util.SpatialHashMap;

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

    private SpatialHashMap spatialMap = new SpatialHashMap(100);

    private QuestDialog questDialog;
    private QuestOverview questOverview;
    private InventoryOverview inventoryOverview;
    public InventoryOverview getInventoryOverview(){ return this.inventoryOverview; }
    private HeartOverview heartOverview;
    private LevelOverview levelOverview;
    private static Game instance;
    private QuestHandler questHandler;
    private Hero hero;
    private DungeonWorld firstLevel;
    private final ArrayList <IEntity> entitiesToRemove = new ArrayList<>();
    private final ArrayList <IEntity> entitiesToAdd = new ArrayList<>();
    private int currentLevelIndex =0;
    private boolean drawTraps=false;

    public ArrayList<DrawableEntity> getEntitiesAtPoint(Point p) {
        return this.spatialMap.getAt(p);
    }

    public ArrayList<DrawableEntity> getEntitiesInCoordRange(Point lowerBound, Point upperBound) {
        return this.spatialMap.getInRange(lowerBound, upperBound);
    }

    public ArrayList<DrawableEntity> getEntitiesInNeighborFields(Point centerPoint) {
        var lowerBound = new Point((float)Math.floor(centerPoint.x) - 1, (float)Math.floor(centerPoint.y) - 1);
        var upperBound = new Point((float)Math.ceil(centerPoint.x) + 1, (float)Math.ceil(centerPoint.y) + 1);
        return this.spatialMap.getInRange(lowerBound, upperBound);
    }

    /**
     * Gets the current level where the hero is
     * @return current level of the game
     */
    public DungeonWorld getCurrentLevel() {
        return levelController.getDungeon();
    }

    /**
     * Hero can see traps if a special potion/scroll has been used
     * @return if traps are drawn
     */
    public boolean getDrawTraps() {
        return drawTraps;
    }

    /**
     * Hero can see traps if a special potion/scroll has been used
     * @param value that determines of traps are drawn
     */
    public void setDrawTraps(boolean value){
        drawTraps= value;
    }

    /**
     * the game is a singleton instance which can be used everywhere in the game
     * @return singleton instance of the game
     */
    public static Game getInstance(){
        if(Game.instance==null){
            Game.instance = new Game();
        }
        return Game.instance;
    }

    /**
     *
     * @return
     */
    public QuestHandler getQuestHandler(){ return this.questHandler; }

    public QuestDialog getQuestDialog() {
        return this.questDialog;
    }

    /**
     *
     * @return
     */
    public Hero getHero(){ return this.hero; }
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
        this.questHandler = new QuestHandler(hero);
        this.entityController.addEntity(this.questHandler);

        firstLevel = null;
        // the entityController will call hero.update each frame
        entityController.addEntity(hero);
        mainLogger.info("Hero created");
        // attach camera to hero
        camera.follow(hero);

        setupGUI();
    }

    private void setupGUI() {
        questDialog = new QuestDialog(hud, textHUD);
        questOverview = new QuestOverview(hud, textHUD);
        inventoryOverview = new InventoryOverview(hud);
        heartOverview = new HeartOverview(hud, textHUD);
        levelOverview = new LevelOverview(textHUD);
    }

    /**
     * Implements logic executed at the begin of a frame.
     */
    @Override
    protected void beginFrame() {
        if (entitiesToAdd.size() > 0) {
            for (IEntity entity : entitiesToAdd) {
                this.entityController.addEntity(entity);
                if (entity instanceof DrawableEntity) {
                    this.spatialMap.insert((DrawableEntity)entity);
                }
            }
            entitiesToAdd.clear();
        }
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
            entityController.removeAllFrom(Trap.class);
            entityController.removeAllFrom(Chest.class);
            entityController.removeAllFrom(QuestGiver.class);
            levelController.triggerNextStage();
            spatialMap.clear();
            mainLogger.info("Next stage loaded");
        }

        // TODO: refactor
        if (hero.isDead()) {
            onGameOver();
        }

        if (entitiesToRemove.size() > 0){
            for(IEntity entity : entitiesToRemove){
                this.entityController.removeEntity(entity);
                if (entity instanceof DrawableEntity) {
                    this.spatialMap.remove((DrawableEntity)entity);
                }
            }
            entitiesToRemove.clear();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            printHashMapStats();
        }
    }

    private void onGameOver() {
        mainLogger.info("GAME OVER");
        hero.onGameOver();
        this.questOverview.hide();
        this.questDialog.hide();
        reloadFirstLevel();
        var allEntities = entityController.getList();
        for(var entity: allEntities){
            if(!(entity instanceof  Hero || entity instanceof QuestHandler)){
                entitiesToRemove.add(entity);
            }
        }
    }

    private void reloadFirstLevel() {
        try {
            levelController.loadDungeon(firstLevel);
            currentLevelIndex =0;
            drawTraps=false;
        } catch (InvocationTargetException ex) {
            mainLogger.severe(ex.getMessage());
        } catch (IllegalAccessException ex) {
            mainLogger.severe(ex.getMessage());
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

        this.spatialMap.remove(this.hero);
        this.spatialMap.insert(this.hero);

        //test_SpawnAllItemsAndMonster();
        spawnEntitiesOfLevel();
    }

    private void spawnEntitiesOfLevel(){
        var levelInfo = new LevelInfo();
        var content = levelInfo.getLevelContent(currentLevelIndex);

        try {
            Spawner.spawnEntities(content,entitiesToAdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an entitty to the game. To prevent a ConcurrentException adding and deleting may
     * only be done in the endframe method.
     *
     * @param entity entity which should be added to the game
     */
    public void addEntity(IEntity entity){
        this.entitiesToAdd.add(entity);
    }
    /**
     * Deletes an entitty to the game. To prevent a ConcurrentException adding and deleting may
     * only be done in the endframe method.
     *
     * @param entity entity which should be added to the game
     */
    public void deleteEntity(IEntity entity){
        this.entitiesToRemove.add(entity);
    }

    /**
     * Spawns all monsters and items at once which are present in the game.
     */
    public void test_SpawnAllItemsAndMonster(){
        DebugControl.SpawnAll(entityController,levelController);
    }


    /**
     * Spawns a monster in the game
     * @param monsterType type of the monster
     * @param position      position of the monster
     * @throws Exception  if monsterType is not supported
     */
    public void spawnMonster(MonsterType monsterType, Point position) throws Exception {
        var monster = Spawner.spawnMonster(monsterType);
        addEntity(monster);
        monster.setLevel(levelController.getDungeon());
        monster.setPosition(position);
    }

    private void printHashMapStats() {
        spatialMap.printStats();

    }
}
