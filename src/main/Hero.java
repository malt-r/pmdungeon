package main;

import InventorySystem.Inventory;
import InventorySystem.Potion;
import InventorySystem.IItemVisitor;
import InventorySystem.Sword1;
import InventorySystem.Spear1;
import InventorySystem.SpecificPotion1;
import InventorySystem.SpecificPotion2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero extends Actor implements IItemVisitor {
    private float healOnKillChance = 0.6f;
    private float healOnKillAmount =  100.f;
    private Inventory inventory;

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
        } else {
            mainLogger.info("Missed " + other.toString());
        }
        if (other.isDead()) {
            mainLogger.info("Other has been slain!");
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
     * @param game Game of the monster
     */
    public Hero(Game game) {
        super(game);
        movementSpeed=0.13f;
        // combat-characteristics:
        health = 200.f;
        maxHealth = 200.f;

        baseHitChance = 0.6f;
        hitChanceModifier = 1.f;

        baseAttackDamage = 50;
        attackDamageModifier = 1.f;

        baseEvasionChance = 0.15f;
        evasionChanceModifier = 1.f;

        knockBackAble = true;

        this.inventory = new Inventory(this, 10);
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
    }

    private void inventoryTest() {
        this.inventory.addItem(new SpecificPotion1(), 3);
        this.inventory.addItem(new SpecificPotion2(), 2);
        this.inventory.addItem(new Spear1(), 1);
        this.inventory.addItem(new Sword1(), 1);
        this.inventory.addItem(new Sword1(), 1);
        this.inventory.addItem(new Sword1(), 1);
        this.inventory.addItem(new SpecificPotion1(), 3);
        this.inventory.addItem(new SpecificPotion2(), 10);

        var item = new Sword1();
        var item1 = new Spear1();
        var item2 = new SpecificPotion1();
        var item3 = new SpecificPotion2();

        item.accept(this);

        //var item = this.inventory.getItemStackAt(0);

        //this.inventory.logContent();

    }

    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        super.update();

        switch (this.movementState) {
            case CAN_MOVE:
                if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    inventoryTest();

                    this.inventory.open(this); // TODO: notify the game, that every actor should be suspended
                    // find chest
                    //chest.inventory.open(this);
                }
                /*if (Gdx.input.isKeyJustPressed(Input.Keys.E)) { // interact with stuff
                    // Find target for interaction and interact
                    // if (target instanceof chest) --> chest.inventory.open();
                    // if (target instanceof item) --> this.inventory.addItem((item)target);
                }*/
                break;
            case IS_KNOCKED_BACK:
                break;
            case SUSPENDED:
                break;
        }
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
    public void visit(Spear1 spear) {
        mainLogger.info("visit spear");
        //equip spear
    }

    @Override
    public void visit(Sword1 sword) {
        //this.equipmentSlotRight(sword);
        //inventory.addItem(oldItem);

        mainLogger.info("visit sword");
    }

    @Override
    public void visit(SpecificPotion1 potion) {
        mainLogger.info("visit potion1");
        // drink potion and affect stats
    }

    @Override
    public void visit(SpecificPotion2 potion) {
        mainLogger.info("visit potion2");
    }
}