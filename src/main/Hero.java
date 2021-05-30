package main;


import GUI.HeroObserver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.tiles.Tile;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;


import items.chests.Chest;
import items.inventory.Inventory;


import items.Item;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.scrolls.SupervisionScroll;
import items.shields.Shield;
import items.weapons.Weapon;
import progress.Level;
import progress.ability.KnockbackAbility;
import progress.ability.SprintAbility;
import quests.QuestReward;
import stats.Modifier;
import util.math.Vec;
import util.math.Convenience;

import java.util.ArrayList;

import static stats.Modifier.*;
import static stats.Attribute.*;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero extends Actor implements items.IInventoryOpener, ObservableHero {
    private float healOnKillChance = 0.6f;
    private float healOnKillAmount =  100.f;
    private Inventory inventory;

    public Inventory getInventory() { return inventory; }

    private boolean inventoryLock = false;
    private Shield leftHandSlot = null; //Defence hand

    public  Shield getLeftHandSlot() { return leftHandSlot; }
    private Weapon rightHandSlot = null; //Offence hand
    public Weapon getRightHandSlot() { return rightHandSlot; }
    private float itemAddDamage = 0.0f;
    private float itemAddDefence = 0.0f;
    private float bonusHealth = 0.0f;
    private float bonusDamage = 0.0f;
    protected int killCount = 0;

    private Level level;
    public Level getLevel(){ return this.level; }
    private boolean invincible = false;

    private ArrayList<HeroObserver> observerList = new ArrayList<HeroObserver>();
    private ArrayList<HeroObserver> observersToRemove = new ArrayList<>();

    public int getKillCount() {
        return killCount;
    }

    // TODO: turn this into an ability
    private void RandomHealOnKill() {
        float rand = (float)Math.random();
        if (rand < healOnKillChance) {
            this.heal(healOnKillAmount);
            mainLogger.info("Hero got healed, health is now " + this.getHealth());
            notifyObservers();
        }
    }

    private float getRangeOfWeapon(){
        return ((rightHandSlot != null) ? rightHandSlot.getRange() : 1.0f);
    }

    @Override
    protected boolean inRangeFunc(Point p){

        if (new Vec(this.getPosition()).subtract(new Vec(p)).magnitude() >= getRangeOfWeapon()) return false;

        Vec direction = new Vec(p).subtract(new Vec(this.getPosition()));
        Vec location = new Vec(this.getPosition());
        float length = direction.magnitude();
        float stepSize = 0.2f;
        int steps = (int) (length / stepSize);

        for (float i = 1.0f; i <= steps; i=i+1.0f){
            Vec stepVector = location.add(direction.multiply(i/(float)steps));

            int x = (int)Math.floor(stepVector.x());
            int y = (int)Math.floor(stepVector.y());

            if (game.getCurrentLevel().getTileTypeAt(x,y) == Tile.Type.WALL){ return false; }
        }
        return true;
    }

    @Override
    public Point lowerAttackRangeBound(Point ownPosition) {
        return new Point((float)Math.floor(ownPosition.x) - getRangeOfWeapon(), (float)Math.floor(ownPosition.y) - getRangeOfWeapon());
    }

    @Override
    public Point upperAttackRangeBound(Point ownPosition) {
        return new Point((float)Math.ceil(ownPosition.x) + 1 + getRangeOfWeapon(), (float)Math.ceil(ownPosition.y) + 1 + getRangeOfWeapon());
    }

    public boolean[] movementLog = new boolean[4];

    private void grantAbility() {
        // TODO: this only works, if the level hits exactly the case
        switch (this.level.getCurrentLevel()) {
            case 2:
                this.level.addAbility(new SprintAbility(() -> Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)));
                mainLogger.info("You gained the sprint ability! Hold down left shift to sprint");
                break;
            case 5:
                this.level.addAbility(new KnockbackAbility(() -> Gdx.input.isKeyJustPressed(Input.Keys.K)));
                mainLogger.info("You gained the knockback ability! Press K to knock back enemies!");
                break;
            default:
                break;
        }
    }

    private void applyLevelUp() {
        mainLogger.info("You leveled up to level " + this.level.getCurrentLevel() + "!");

        var dmgIncr = this.level.getDamageIncrementForCurrentLevel();
        this.stats.applyModToAttribute
        (
            new Modifier(dmgIncr, ModifierType.ADDITION, AttributeType.PHYSICAL_ATTACK_DAMAGE)
        );

        mainLogger.info("Your base attack damage is now " + this.stats.getValue(AttributeType.PHYSICAL_ATTACK_DAMAGE));

        var healthIncr = this.level.getHealthIncrementForCurrentLevel();
        this.stats.applyModToAttribute
        (
            new Modifier(healthIncr, ModifierType.ADDITION, AttributeType.MAX_HEALTH)
        );

        mainLogger.info("Your max health is now " + this.stats.getValue(AttributeType.MAX_HEALTH));
        grantAbility();
    }

    /**
     *  Manages attacking of another actor.
     *  @param other The actor that should be attacked
     *  @return true if the attack was sucessfull
     */
    @Override
    public float attack(ICombatable other) {
        float damage = super.attack(other);
        if (damage > 0.0f) {
            mainLogger.info(damage + " damage dealt to " + other.toString());
            if (rightHandSlot != null) {

                // TODO: use effect for that?
                if (!rightHandSlot.reduceCondition(25)) {
                    mainLogger.info("Das Schwert ist hinüber");
                    rightHandSlot = null;
                }
            }
        } else {
            mainLogger.info("Missed " + other.toString());
        }
        if (other.isDead()) {
            mainLogger.info("Other has been slain!");

            // TODO: specify xp amount based on monster kind
            boolean levelIncrease = this.level.increaseXP(75);

            mainLogger.info("Current XP: " + level.getCurrentXP());
            mainLogger.info("XP to next Level: " + level.getXPForNextLevelLeft());

            RandomHealOnKill();

            this.killCount++;
            notifyObservers();
        }
        return damage;
    }
    /**
     * Manages damage given by other ICombatable instances.
     *
     * @param damage  The damage value that should be deducted from health.
     */
    @Override
    public void dealDamage(float damage, ICombatable attacker) {

        //TODO - Reduce life of shield if equipped
        if (!this.invincible) {
            super.dealDamage(damage, attacker);
        }
        mainLogger.info(this.toString() + ": " + this.getHealth() + " health left");

        notifyObservers();

    }

    private void CoordHelper() {
        mainLogger.info("Hero Coord: (" + this.getPosition().x + "|" + this.getPosition().y + ")" );
    }

    private void PrintNearEntities() {
        CoordHelper();

        Point lowerBound = new Point(this.getPosition().x - 1, this.getPosition().y - 1);
        Point upperBound = new Point(this.getPosition().x + 1, this.getPosition().y + 1);
        var entities = Game.getInstance().getEntitiesInCoordRange(lowerBound, upperBound);

        for (var entity : entities) {
            mainLogger.info("Found entity " + entity.toString() + " at position (" + entity.getPosition().x + "|" + entity.getPosition().y + ")" );
        }
    }

    private void toggleGodMode() {
        this.invincible = !this.invincible;

        if (this.invincible) {
            this.level.addAbility(new SprintAbility(() -> Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)));
            this.level.addAbility(new KnockbackAbility(() -> Gdx.input.isKeyJustPressed(Input.Keys.K)));
            mainLogger.info("God mode on");
        } else {
            mainLogger.info("God mod off");
        }
    }

    /**
     * Constructor of the Hero class.
     * <p>
     * This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Hero() {
        super();
        this.stats.addInPlace(AttributeType.MOVEMENT_SPEED, 0.13f);
        this.stats.addInPlace(AttributeType.HEALTH, 200.f);
        this.stats.addInPlace(AttributeType.MAX_HEALTH, 200.f);
        this.stats.addInPlace(AttributeType.HIT_CHANCE, 0.7f);
        this.stats.addInPlace(AttributeType.PHYSICAL_ATTACK_DAMAGE, 50.f);
        this.stats.addInPlace(AttributeType.EVASION_CHANCE, 0.15f);

        knockBackAble = true;

        this.inventory = new Inventory(this, 10);
        this.level = new Level(this::applyLevelUp);
    }

    /**
     * Generates the run and idle animation for the hero.
     */
    @Override
    protected void generateAnimations(){
        String[] idleLeftFrames = new String[]{
                "tileset/hero/knight_m_idle_left_anim_f0.png",
                "tileset/hero/knight_m_idle_left_anim_f1.png",
                "tileset/hero/knight_m_idle_left_anim_f2.png",
                "tileset/hero/knight_m_idle_left_anim_f3.png"
        };
        idleAnimationLeft = createAnimation(idleLeftFrames, 6);

        String[] idleRightFrames = new String[]{
                "tileset/hero/knight_m_idle_anim_f0.png",
                "tileset/hero/knight_m_idle_anim_f1.png",
                "tileset/hero/knight_m_idle_anim_f2.png",
                "tileset/hero/knight_m_idle_anim_f3.png"
        };
        idleAnimationRight = createAnimation(idleRightFrames, 6);

        String[] runLeftFrames = new String[]{
                "tileset/hero/knight_m_run_left_anim_f0.png",
                "tileset/hero/knight_m_run_left_anim_f1.png",
                "tileset/hero/knight_m_run_left_anim_f2.png",
                "tileset/hero/knight_m_run_left_anim_f3.png"
        };
        runAnimationLeft = createAnimation(runLeftFrames, 4);

        String[] runRightFrames = new String[]{
                "tileset/hero/knight_m_run_anim_f0.png",
                "tileset/hero/knight_m_run_anim_f1.png",
                "tileset/hero/knight_m_run_anim_f2.png",
                "tileset/hero/knight_m_run_anim_f3.png"
        };
        runAnimationRight = createAnimation(runRightFrames, 4);

        String[] hitLeftFrames = new String[]{
                "tileset/hero/knight_m_idle_left_anim_f0_hit.png"
        };
        hitAnimationLeft = createAnimation(hitLeftFrames, 1);

        String[] hitRightFrames = new String[]{
                "tileset/hero/knight_m_idle_anim_f0_hit.png"
        };
        hitAnimationRight = createAnimation(hitRightFrames, 1);
    }

    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        super.update();

        switch (this.movementState) {
            case CAN_MOVE:
                if (!inventoryLock) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                        this.inventory.open(this);
                    }
                    this.level.checkForAbilityActivation(this);
                }
                break;
            case IS_KNOCKED_BACK:
                break;
            case SUSPENDED:
                break;
        }

        this.inventory.update();
        removeObserversToRemove();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            CoordHelper();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            PrintNearEntities();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            toggleGodMode();
        }
    }

    @Override
    public void setLevel(DungeonWorld level) {
        super.level = level;
        findRandomPosition();
    }

    /**
     * Resets the combat stats of the hero-
     */
    @Override
    protected void resetStats() {
        super.resetStats();
        mainLogger.info("Stats reset");
        notifyObservers();
    }

    public void onGameOver() {
        resetStats();
        this.killCount = 0;
        this.rightHandSlot = null;
        this.leftHandSlot = null;
        this.inventory.clear();
        this.level.reset();
        notifyObservers();
    }

    /**
     * Generates Movement Input, depending on the pressed key on the keyboard.
     */
    @Override
    protected Point readMovementInput(){
        var newPosition = new Point(this.getPosition());
        var movementSpeed = this.stats.getValue(AttributeType.MOVEMENT_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newPosition.y += movementSpeed;
        }else{
            if(movementLog[0]){mainLogger.fine("W press stopped");}
            movementLog[0] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newPosition.y -= movementSpeed;
        }else{
            if(movementLog[1]){mainLogger.fine("S press stopped");}
            movementLog[1] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newPosition.x += movementSpeed;
        }else{
            if(movementLog[2]){mainLogger.fine("D press stopped");}
            movementLog[2] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newPosition.x -= movementSpeed;
        }else{
            if(movementLog[3]){mainLogger.fine("A press stopped");}
            movementLog[3] = false;
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.W))){
            mainLogger.fine("W press started");
            movementLog[0] = true;
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.S))){
            mainLogger.fine("S press started");
            movementLog[1] = true;
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.D))){
            mainLogger.fine("D press started");
            movementLog[2] = true;
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.A))){
            mainLogger.fine("A press started");
            movementLog[3] = true;
        }

        return newPosition;
    }

    /**
     * Generates pickup input, depending on the pressed key on the keyboard.
     */
    @Override
    protected boolean readPickupInput(){
        if (!inventoryLock) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
                return true;
            }
        }
        return false;
    }

    /**
     * handles picking up an item, from ground into inventory and from chest into inventory
     */
    @Override
    protected void handleItemPicking(){
        var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
        for (IEntity entity : nearEntities) {
            if (entity instanceof Item) {
                var item = (Item) entity;
                if(Convenience.checkForIntersection(this.getPosition(), item.getPosition())) {
                    if (inventory.addItem(item)){
                        game.deleteEntity(entity);
                    }
                    break;
                }
            } else if (entity instanceof Chest) {
                var chest = (Chest) entity;
                if(Convenience.checkForIntersection(this.getPosition(), chest.getPosition())) {
                    System.out.println("Opening chest");
                    chest.open(this);
                    break;
                }
            }
        }
    }

    @Override
    public boolean attackOnInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            return true;
        }
        return false;
    }

    private void updateWeaponModifiers(Weapon oldWeapon, Weapon newWeapon){
        if (null != oldWeapon) {
            for (var mod : oldWeapon.getModifiers()) {
                this.stats.removeModifierFromAttribut(mod);
            }
        }

        if (null != newWeapon) {
            for (var mod : newWeapon.getModifiers()) {
                this.stats.applyModToAttribute(mod);
            }
        }
    }

    private void updateShieldStats(Shield oldShield, Shield newShield){
        if (null != oldShield) {
            for (var mod : oldShield.getModifiers()) {
                this.stats.removeModifierFromAttribut(mod);
            }
        }

        if (null != newShield) {
            for (var mod : newShield.getModifiers()) {
                this.stats.applyModToAttribute(mod);
            }
        }
    }

    /**
     * Visits a weapon item
     * @param weapon weapon which should be visited
     */
    @Override
    public void visit(Weapon weapon){
        //TODO - Error if Inventory is full!
        updateWeaponModifiers(rightHandSlot, weapon);
        if (rightHandSlot != null) { inventory.addItem(rightHandSlot); }
        rightHandSlot = weapon;
        mainLogger.info("visit weapon");
        notifyObservers();
    }

    /**
     * Visits a shield itme
     * @param shield weapon which should be visited
     */
    @Override
    public void visit(Shield shield){
        updateShieldStats(leftHandSlot, shield);
        if (leftHandSlot != null) { inventory.addItem(leftHandSlot); }
        leftHandSlot = shield;
        mainLogger.info("visit shield");
        notifyObservers();
    }

    /**
     * Visits a Healpotion item
     * @param potion weapon which should be visited
     */
    @Override
    public void visit(HealthPotion potion) {
        this.heal(potion.getHealValue());
        mainLogger.info("visit potion1");
        notifyObservers();
    }

    /**
     * Visits a PoisonPotion item
     * @param potion weapon which should be visited
     */
    @Override
    public void visit(PoisonPotion potion){
        //TODO: heal with a negative
        this.heal(-potion.getDamageValue());
        mainLogger.info("visit potion2");
        notifyObservers();
    }

    /**
     * Visits a AttackScroll item
     * @param scroll weapon which should be visited
     */
    @Override
    public void visit(AttackScroll scroll) {
        //TODO: AttackScroll should not heal
        //this.heal(scroll.getAttackBonus());
        mainLogger.info("visit Attackscroll");
    }

    /**
     *  Visits a SpeedScroll item
     * @param scroll scroll which should be visited
     */
    @Override
    public void visit(SpeedScroll scroll){
        mainLogger.info("visit speedscroll");
    }
    /**
     *  Visits a SupervisionScroll item
     * @param scroll scroll which should be visited
     */
    @Override
    public void visit(SupervisionScroll scroll) {
        game.setDrawTraps(true);
        mainLogger.info("Traps can now be seen");
    }

    @Override
    public boolean addItemToInventory(Item item) {
        return this.inventory.addItem(item);
    }

    @Override
    public int getNumFreeSlotsOfInventory() {
        return this.inventory.getNumFreeSlots();
    }

    @Override
    public boolean lock() {
        if (!this.inventoryLock) {
            this.inventoryLock = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean unlock() {
        if (this.inventoryLock) {
            this.inventoryLock = false;
            return true;
        }
        return false;
    }

    /**
     * Registers an observer
     * @param observer to be registered
     */
    @Override
    public void register(HeroObserver observer) {
        this.observerList.add(observer);
    }

    /**
     * Unregisters an observer
     * @param observer to be unregistered
     */
    @Override
    public void unregister(HeroObserver observer) {
        if (this.observerList.contains(observer) && !this.observersToRemove.contains(observer)) {
            this.observersToRemove.add(observer);
        }
    }

    private void removeObserversToRemove() {
        for (HeroObserver heroObserver : observersToRemove) {
            this.observerList.remove(heroObserver);
        }
        this.observersToRemove.clear();
    }

    /**
     * notifies all observers
     */
    @Override
    public void notifyObservers() {
        for(HeroObserver obs : observerList){
            if (!observersToRemove.contains(obs)) {
                obs.update(this);
            }
        }
    }

    public void applyReward(QuestReward reward) {
        if (null != reward) {
            // apply xp
            this.level.increaseXP(reward.getXp());

            // add items to inventory or drop them on the floor
            int freeSlots = this.inventory.getNumFreeSlots();
            var rewardItems = reward.getItems();
            if (freeSlots < rewardItems.size()) {
                for (int i = 0; i < freeSlots; i++) {
                    this.inventory.addItem(rewardItems.get(i));
                }
                for (int i = freeSlots; i < rewardItems.size(); i++){
                    inventory.dropItem(rewardItems.get(i));
                }
            } else {
                for (Item item : rewardItems) {
                    this.inventory.addItem(item);
                }
            }
        }
    }
}