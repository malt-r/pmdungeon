package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.*;
import java.util.logging.Logger;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero extends Actor {
    static Logger l = Logger.getLogger(Hero.class.getName());

    // implementation of ICombatable -----------------------------------------------------------------------------------
    private final float movementSpeed = 0.1f;
    // cache a reference to the game to be able to scan all entities for possible attack targets
    private ICombatable target;

    // combat-characteristics:
    float health = 100.f;
    float maxHealth = 100.f;

    float baseHitChance = 0.6f;
    float hitChanceModifier = 1.f;

    float baseAttackDamage = 50;
    float attackDamageModifier = 1.f;

    float baseEvasionChance = 0.15f;
    float evasionChanceModifier = 1.f;

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public boolean hasTarget() {
        return this.target != null;
    }

    @Override
    public ICombatable getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(ICombatable target) {
        this.target = target;
    }

    @Override
    public float getHitChance() {
        return baseHitChance * hitChanceModifier;
    }

    @Override
    public float getEvasionChance() {
        return this.baseEvasionChance * evasionChanceModifier;
    }

    @Override
    public float getDamage() {
        return this.baseAttackDamage * this.attackDamageModifier;
    }

    @Override
    public boolean attack(ICombatable other) {
        boolean success = super.attack(other);
        if (success) {
            l.info("Hit " + other.toString());
        } else {
            l.info("Missed " + other.toString());
        }
        if (other.isDead()) {
            l.info("Other has been slain!");
            // here would the hero gain experience...
        }
        return success;
    }


    @Override
    public void dealDamage(float damage) {
        super.dealDamage(damage);

        if (isDead()) {
            l.info("GAME OVER");
        }
    }
    // end of implementation of ICombatable ----------------------------------------------------------------------------

    /**
     * Constructor of the Hero class.
     * <p>
     * This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Hero(Game game) {
        super(game);
    }
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

    private Animation createAnimation(String[] texturePaths, int frameTime) {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {

            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }



    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        super.update();
    }

    protected void resetCombatStats() {
        super.resetCombatStats();
        l.info("Hero: resetting combat stats");
    }

    @Override
    protected Point readMovementInput(){
        var newPosition = new Point(this.position);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newPosition.y += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newPosition.y -= movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newPosition.x += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newPosition.x -= movementSpeed;
        }
        return newPosition;
    }

}