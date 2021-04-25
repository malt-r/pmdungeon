package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero extends Actor {
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
    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        super.update();
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
}