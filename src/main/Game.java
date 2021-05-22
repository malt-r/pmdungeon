package main;

import GUI.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.Item;
import items.chests.Chest;
import items.inventory.*;
import main.sample.DebugControl;
import monsters.Monster;
import monsters.MonsterType;
import progress.Level;
import quests.KillMonstersQuest;
import quests.QuestGiver;
import quests.QuestHandler;
import traps.*;
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
     * @return singelton instance of the game
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

    public QuestOverview getQuestOverview() {
        return this.questOverview;
    }

    //TODO - Maybe only temporary (who knows)
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
        // TODO: temporary solution, how to pass hero to quest?
        //this.questHandler.requestForNewQuest(new KillMonstersQuest(this.hero));

        firstLevel = null;
        // the entityController will call hero.update each frame
        entityController.addEntity(hero);
        mainLogger.info("Hero created");
        // attach camera to hero
        camera.follow(hero);


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
//                if(entity instanceof Actor) {
//                    ((Actor)entity).setLevel(levelController.getDungeon());
//                }
//                if(entity instanceof Item) {
//                    ((Item)entity).setLevel(levelController.getDungeon());
//                }
//                if(entity instanceof Chest) {
//                    ((Chest)entity).setLevel(levelController.getDungeon());
//                }
//                if(entity instanceof Trap) {
//                    ((Trap)entity).setLevel(levelController.getDungeon());
//                }
            }
            entitiesToAdd.clear();
        }

        // TODO: this still needed?
        //Y in German keyboard
       // if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
       //     questDialog.show();
       // }
       // if (Gdx.input.isKeyPressed(Input.Keys.X)) {
       //     questDialog.hide();
       // }

       // if (Gdx.input.isKeyPressed(Input.Keys.C)) {
       //     questOverview.show();
       // }
       // if (Gdx.input.isKeyPressed(Input.Keys.V)) {
       //     questOverview.hide();
       // }
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
            mainLogger.info("Next stage loaded");

        }

        if (hero.isDead()) {
            mainLogger.info("GAME OVER");
            hero.onGameOver();
            try {
                levelController.loadDungeon(firstLevel);
                currentLevelIndex =0;
                drawTraps=false;
            } catch (InvocationTargetException ex) {
                mainLogger.severe(ex.getMessage());
            } catch (IllegalAccessException ex) {
                mainLogger.severe(ex.getMessage());
            }
            var allEntities = getAllEntities();
            for(var entity: allEntities){
                if(!(entity instanceof  Hero)){
                    entitiesToRemove.add(entity);
                }
            }

            //spawnEntitiesOfLevel();
        }

        if (entitiesToRemove.size() > 0){
            for(IEntity entity : entitiesToRemove){
                this.entityController.removeEntity(entity);
            }
            entitiesToRemove.clear();
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
     * Returns all entities from the entityController.
     * This method is used by the combat system to enable ICombatable-instances to scan for attackable targets.
     * @return List of all entities in the game.
     */
    public ArrayList<IEntity> getAllEntities() {
        return this.entityController.getList();
    }

    // TODO: find better place / name/ for this, don't hardcode hero as the IDrawable to check against
    /**
     *
     * @param p Point which should be checked if it collids with the hero
     * @return if point is on the same tile as the hero
     */
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

    /**
     * A generic trigger function which checks if two IDrawable instances are on the same time
     * @param drawable1 first drawable
     * @param drawable2 second drawable
     * @param level     dungeon level
     * @return          if the two drawables are on the same tile in the same level
     */
    public boolean checkForIntersection (IDrawable drawable1, IDrawable drawable2, DungeonWorld level) {
        int ownX = Math.round(drawable1.getPosition().x);
        int ownY = Math.round(drawable1.getPosition().y);
        var ownTile = level.getTileAt(ownX, ownY);
        Point otherPosition = drawable2.getPosition();

        int otherX = Math.round(otherPosition.x);
        int otherY = Math.round(otherPosition.y);
        var otherTile = level.getTileAt(otherX, otherY);
        return ownTile == otherTile;
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
        monster.position = position;
    }


}
