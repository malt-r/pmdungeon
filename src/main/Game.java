package main;

import GUI.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.Item;
import items.inventory.*;
import main.sample.DebugControl;
import monsters.Monster;
import monsters.MonsterType;
import progress.Level;
import traps.*;
import java.util.logging.Logger;

/**
 * The main game class.
 * <p>
 *     This class implements the MainController, which handles high level game logic, like the
 *     setup method and calling of the game loop.
 * </p>
 */
public class Game extends MainController implements InventoryObserver, HeroObserver, LevelObserver, OpenStateObserver {
    private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    //GUI
    private HeartIcon[] hearts = new HeartIcon[10];
    private InventoryIcon[] inventory = new InventoryIcon[10];
    private InventoryIcon[] chest = new InventoryIcon[10];
    private InventoryIcon[] heroSlots = new InventoryIcon[2];
    private InvBackgroundIcon[] invBackground = new InvBackgroundIcon[10];
    private InvBackgroundIcon[] chestBackground = new InvBackgroundIcon[10];
    private Label expLabel;
    private Label heartLabel;

    private static Game instance;
    private Hero hero;
    private DungeonWorld firstLevel;
    private final ArrayList <IEntity> entitiesToRemove = new ArrayList<>();
    private final ArrayList <IEntity> entitiesToAdd = new ArrayList<>();
    private int currentLevelIndex =0;
    private boolean drawTraps=false;

    public DungeonWorld getCurrentLevel() {
        return levelController.getDungeon();
    }

    public boolean getDrawTraps() {
        return drawTraps;
    }
    public void setDrawTraps(boolean value){
        drawTraps= value;
    }

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

        //GUI
        for (int i = 0; i < 10; i++){

            invBackground[i] = new InvBackgroundIcon(i, 0.0f);
            invBackground[i].setDefaultBackgroundTexture();
            hud.addHudElement(invBackground[i]);

            chestBackground[i] = new InvBackgroundIcon(i, 1.0f);
            hud.addHudElement(chestBackground[i]);

            hearts[i] = new HeartIcon(i);
            hud.addHudElement(hearts[i]);

            inventory[i] = new InventoryIcon(i, 0.0f);
            hud.addHudElement(inventory[i]);

            chest[i] = new InventoryIcon(i, 1.0f);
            hud.addHudElement(chest[i]);

        }

        for (int i = 0; i < 2; i++){
            heroSlots[i] = new InventoryIcon( i + 11, 0.0f);
            hud.addHudElement(heroSlots[i]);
        }

        expLabel = textHUD.drawText(hero.getLevel().getCurrentLevel() + "    " +
                                    hero.getLevel().getCurrentXP() + "/" +
                                    hero.getLevel().getXPForNextLevelTotal(),
                            "fonts/Pixeled.ttf", Color.YELLOW, 20,20,20,5,400);

        heartLabel = textHUD.drawText("","fonts/Pixeled.ttf",
                                    Color.RED, 20,20,20,50,445);

        heartCalc();

        //Register Observer
        hero.getInventory().register(this);
        hero.register(this);
        hero.getLevel().register(this);

    }

    /**
     * Implements logic executed at the begin of a frame.
     */
    @Override
    protected void beginFrame() {
        if (entitiesToAdd.size() > 0) {
            for (IEntity entity : entitiesToAdd) {
                this.entityController.addEntity(entity);
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


    public void spawnMonster(MonsterType monsterType, Point position) throws Exception {
        var monster = Spawner.spawnMonster(monsterType);
        addEntity(monster);
        monster.setLevel(levelController.getDungeon());
        monster.position = position;
    }

    private void heartCalc(){
        float health = hero.getHealth();
        int heartHalves = (int) Math.ceil(health/10);
        int heartFull = (int)heartHalves/2;

        heartLabel.setText("");


        if (health <= 200.0f){
            int i = 0;

            for (i = 0; i < heartFull; i++){
                hearts[i].setState(2);
            }
            if (heartHalves%2 == 1){
                hearts[i].setState(1);
                i++;
            }
            for (int j = i; j < 10; j++){
                hearts[j].setState(0);
            }
        }else{
            hearts[0].setState(2);
            for (int i = 1; i < hearts.length; i++){
                hearts[i].setDefaultTexture();
            }

            heartLabel.setText("" + heartFull);

        }
    }

    @Override
    public void update(Inventory inv, boolean fromHero){
        if (fromHero){
            //TODO - Pointer to current selected inv slot
            if (inv.getCurrentState() instanceof OwnInventoryOpenState){
                ((OwnInventoryOpenState)inv.getCurrentState()).register(this);

                invBackground[((OwnInventoryOpenState) inv.getCurrentState()).getselectorIdx()].setPointerTexture();
            } else {
                for(int i = 0; i < invBackground.length; i++){
                    invBackground[i].setDefaultBackgroundTexture();
                }
            }
            for (int i = 0; i < inventory.length; i++){
                if (i < inv.getCount()){
                    inventory[i].setTexture(inv.getItemAt(i).getTexture());
                } else {
                    inventory[i].setDefaultTexture();
                }
            }
        }else{
            if (inv.getCurrentState() instanceof OtherInventoryOpenState) {
                for (int i = 0; i < chest.length; i++){
                    chestBackground[i].setDefaultBackgroundTexture();
                }
                ((OtherInventoryOpenState)inv.getCurrentState()).register(this);
                chestBackground[((OtherInventoryOpenState) inv.getCurrentState()).getselectorIdx()].setPointerTexture();

                for (int i = 0; i < chest.length; i++){
                    if (i < inv.getCount()){
                        chest[i].setTexture(inv.getItemAt(i).getTexture());
                    } else {
                        chest[i].setDefaultTexture();
                    }
                }
            }else {//if (inv.getCurrentState() instanceof InventoryClosedState){
                for (int i = 0; i < chest.length; i++) {
                    chest[i].setDefaultTexture();
                    chestBackground[i].setDefaultTexture();
                }
            }
        }
    }

    @Override
    public void update(Hero hero) {

        Item leftHand = hero.getLeftHandSlot();
        Item rightHand = hero.getRightHandSlot();
        if (leftHand != null){
            heroSlots[0].setTexture(leftHand.getTexture());
        }else{
            heroSlots[0].setDefaultTexture();
        }

        if (rightHand != null){
            heroSlots[1].setTexture(rightHand.getTexture());
        }else{
            heroSlots[1].setDefaultTexture();
        }

        heartCalc();
    }

    @Override
    public void update(Level level) {
        expLabel.setText(level.getCurrentLevel() + " " + level.getCurrentXP() + "/" + level.getXPForNextLevelTotal());
    }

    @Override
    public void update(InventoryOpenState invOp) {
        if (invOp instanceof OwnInventoryOpenState){
            for(int i = 0; i < invBackground.length; i++){
                invBackground[i].setDefaultBackgroundTexture();
            }
            invBackground[invOp.getselectorIdx()].setPointerTexture();
        }else if (invOp instanceof OtherInventoryOpenState){
            for(int i = 0; i < invBackground.length; i++){
                chestBackground[i].setDefaultBackgroundTexture();
            }
            chestBackground[invOp.getselectorIdx()].setPointerTexture();
        }

    }
}
