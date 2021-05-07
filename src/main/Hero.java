package main;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;


import items.Chest;
import items.IInventoryOpener;
import items.inventory.Inventory;


import items.Item;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.shields.Shield;
import items.weapons.Weapon;
import progress.Level;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero extends Actor implements items.IInventoryOpener {
    private float healOnKillChance = 0.6f;
    private float healOnKillAmount =  100.f;
    private Inventory inventory;
    private boolean inventoryLock = false;
    private Item leftHandSlot = null; //Defence hand
    private Weapon rightHandSlot = null; //Offence hand
    private float itemAddDamage = 0.0f;
    private float itemAddDefence = 0.0f;

    private Level level;

    private void RandomHealOnKill() {
        float rand = (float)Math.random();
        if (rand < healOnKillChance) {
            this.heal(healOnKillAmount);
            mainLogger.info("Hero got healed, health is now " + this.health);
        }
    }

    public boolean[] movementLog = new boolean[4];
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
                if (!rightHandSlot.reduceCondition(25)) {
                    mainLogger.info("Das Schwert ist hinüber");
                    rightHandSlot = null;
                    attackDamageModifierWeapon = 1.0f;
                    hitChanceModifierWeapon = 1.0f;
                }
            }
        } else {
            mainLogger.info("Missed " + other.toString());
        }
        if (other.isDead()) {
            mainLogger.info("Other has been slain!");

            // TODO: specify xp amount based on monster kind
            boolean levelIncrease = this.level.increaseXP(50);
            if (levelIncrease) {
                // apply level up bonus
                // increase max health, attack damage
                // grant special ability at specific level up points
            }

            // here would the hero gain experience...
            RandomHealOnKill();
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
        super.dealDamage(damage, attacker);
        mainLogger.info(this.toString() + ": " + health + " health left");

        if (isDead()) {
            mainLogger.info("GAME OVER");
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
        movementSpeed=0.13f;
        // combat-characteristics:
        health = 200.f;
        maxHealth = 200.f;

        baseHitChance = 0.6f;
        hitChanceModifierWeapon = 1.f;
        hitChanceModifierScroll = 1.f;

        baseAttackDamage = 50;
        attackDamageModifierWeapon = 1.f;
        attackDamageModifierScroll = 1.f;

        baseEvasionChance = 0.15f;
        evasionChanceModifierWeapon = 1.f;
        evasionChanceModifierScroll = 1.f;

        knockBackAble = true;

        this.inventory = new Inventory(this, 10);
        this.level = new Level();
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

        String[] hitAnimationFrames = new String[]{
                "tileset/hero/knight_m_hit_anim_f0.png",
                "tileset/hero/knight_m_hit_anim_f0.png",
                "tileset/hero/knight_m_hit_anim_f0.png"
        };
        hitAnimation = createAnimation(hitAnimationFrames, 3);
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
                }
                break;
            case IS_KNOCKED_BACK:
                break;
            case SUSPENDED:
                break;
        }

        this.inventory.update();
    }

    /**
     * Resets the combat stats of the hero-
     */
    @Override
    protected void resetCombatStats() {
        super.resetCombatStats();
        mainLogger.info("Combat stats reset");
    }
    /**
     * Generates Movement Input, depending on the pressed key on the keyboard.
     */
    @Override
    protected Point readMovementInput(){
        var newPosition = new Point(this.position);
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

    @Override
    protected boolean readPickupInput(){
        if (!inventoryLock) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void handleItemPicking(){
        var allEntities = game.getAllEntities();
        for (IEntity entity : allEntities) {
            if (entity instanceof Item) {
                var item = (Item) entity;
                if(game.checkForTrigger(item.getPosition())){
                    inventory.addItem(item);
                    game.deleteEntity(entity);
                    System.out.println(item.getName());
                    break;
                }
            } else if (entity instanceof Chest) {
                var chest = (Chest) entity;
                if(game.checkForTrigger(chest.getPosition())) {
                    System.out.println("Opening chest");
                    chest.open(this);
                    break;
                }
            }
        }
    }

    @Override
    protected boolean readCombatInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            return true;
        }
        return false;
    }

    private void updateStats(Weapon weapon){
        attackDamageModifierWeapon = weapon.getAttackDamageModifier(); //When absolut
        hitChanceModifierWeapon = weapon.getHitChanceModifier();
    }

    private void updateStats(Shield shield){
        evasionChanceModifierWeapon = shield.getDefenseValue();
    }

    @Override
    public void visit(Weapon weapon){
        if (rightHandSlot != null) { inventory.addItem(rightHandSlot); }
        rightHandSlot = weapon;
        updateStats(weapon);
        mainLogger.info("visit weapon");
    }

    @Override
    public void visit(Shield shield){
        if (leftHandSlot != null) { inventory.addItem(rightHandSlot); }
        leftHandSlot = shield;
        updateStats(shield);
        mainLogger.info("visit shield");
    }

    @Override
    public void visit(HealthPotion potion) {
        this.heal(potion.getHealValue());
        mainLogger.info("visit potion1");
    }

    @Override
    public void visit(PoisonPotion potion){
        //TODO: heal with a negative
        this.heal(-potion.getDamageValue());
        mainLogger.info("visit potion2");
    }

    @Override
    public void visit(AttackScroll scroll) {
        this.heal(scroll.getAttackBonus());
        mainLogger.info("visit Attackscroll");
    }

    @Override
    public void visit(SpeedScroll scroll){
        mainLogger.info("visit speedscroll");
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
}