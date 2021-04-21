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
public class Hero implements IAnimatable, IEntity, ICombatable {
    static Logger l = Logger.getLogger(Hero.class.getName());
    private Point position;
    private DungeonWorld level;

    private final Animation idleAnimationRight;
    private final Animation idleAnimationLeft;
    private final Animation runAnimationLeft;
    private final Animation runAnimationRight;
    private Animation currentAnimation;

    // currently only two looking directions are supported (left and right),
    // therefore a boolean is sufficient to represent the
    // looking direction
    private boolean lookLeft;

    // implementation of ICombatable -----------------------------------------------------------------------------------
    private final Timer attackTimer;
    private final long attackDelay = 1000;
    private boolean canAttack;

    // cache a reference to the game to be able to scan all entities for possible attack targets
    private final Game game;
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
        boolean success = ICombatable.super.attack(other);
        if (success) {
            l.info("Hit " + other.toString());
        } else {
            l.info("Missed " + other.toString());
        }

        if (other.isDead()) {
            l.info("Other has been slain!");
            // here would the hero gain experience...
        }

        // delay next attack by attackDelay ms
        this.canAttack = false;
        TimerTask resetCanAttackTask = new TimerTask() {
            @Override
            public void run() {
                canAttack = true;
            }
        };
        attackTimer.schedule(resetCanAttackTask, attackDelay);
        return success;
    }

    @Override
    public boolean canAttack() {
        return canAttack;
    }

    @Override
    public void dealDamage(float damage) {
        ICombatable.super.dealDamage(damage);

        if (isDead()) {
            l.info("GAME OVER");
        }
    }

    @Override
    public void heal(float amount) {
        this.health += amount;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    private enum AnimationState {
        IDLE,
        RUN,
    }

    // end of implementation of ICombatable ----------------------------------------------------------------------------

    /**
     * Constructor of the Hero class.
     * <p>
     *     This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Hero(Game game) {
        this.game = game;

        String[] idleLeftFrames = new String[] {
                "tileset/hero/knight_m_idle_left_anim_f0.png",
                "tileset/hero/knight_m_idle_left_anim_f1.png",
                "tileset/hero/knight_m_idle_left_anim_f2.png",
                "tileset/hero/knight_m_idle_left_anim_f3.png"
        };
        idleAnimationLeft = createAnimation(idleLeftFrames, 6);

        String[] idleRightFrames = new String[] {
                "tileset/hero/knight_m_idle_anim_f0.png",
                "tileset/hero/knight_m_idle_anim_f1.png",
                "tileset/hero/knight_m_idle_anim_f2.png",
                "tileset/hero/knight_m_idle_anim_f3.png"
        };
        idleAnimationRight = createAnimation(idleRightFrames, 6);

        String[] runLeftFrames = new String[] {
                "tileset/hero/knight_m_run_left_anim_f0.png",
                "tileset/hero/knight_m_run_left_anim_f1.png",
                "tileset/hero/knight_m_run_left_anim_f2.png",
                "tileset/hero/knight_m_run_left_anim_f3.png"
        };
        runAnimationLeft = createAnimation(runLeftFrames, 4);

        String[] runRightFrames = new String[] {
                "tileset/hero/knight_m_run_anim_f0.png",
                "tileset/hero/knight_m_run_anim_f1.png",
                "tileset/hero/knight_m_run_anim_f2.png",
                "tileset/hero/knight_m_run_anim_f3.png"
        };
        runAnimationRight = createAnimation(runRightFrames, 4);

        lookLeft = false;
        canAttack = true;
        attackTimer = new Timer();
    }

    private Animation createAnimation(String[] texturePaths, int frameTime)
    {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {

            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }

    /**
     * Determine the active animation which should be played.
     * @return The active animation.
     */
    @Override
    public Animation getActiveAnimation() {
        return this.currentAnimation;
    }

    private void setCurrentAnimation(AnimationState animationState) {
        switch (animationState) {
            case RUN:
                this.currentAnimation =  lookLeft ? this.runAnimationLeft : this.runAnimationRight;
                break;
            case IDLE:
            default:
                this.currentAnimation =  lookLeft ? this.idleAnimationLeft : this.idleAnimationRight;
        }
    }

    /**
     * Get the current position in the DungeonWorld.
     * @return the current position in the DungeonWorld.
     */
    @Override
    public Point getPosition() {
        return position;
    }

    /**
     * Normalize the difference-vector between two Points on a defined basis.
     * @param p1 The origin of the vector
     * @param p2 The tip of the vector
     * @param normalizationBasis The basis on which the length of the difference-vector should be normalized.
     * @return A Point, of which the x and y members represent the components of the normalized vector.
     */
    private Point normalizeDelta(Point p1, Point p2, float normalizationBasis) {
        float diffX = p2.x - p1.x;
        float diffY = p2.y - p1.y;
        float magnitude = (float)Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

        if (magnitude != 0.0f) {
            var diffXNorm = diffX / magnitude * normalizationBasis;
            var diffYNorm = diffY / magnitude * normalizationBasis;
            return new Point(diffXNorm, diffYNorm);
        } else {
            return new Point(diffX, diffY);
        }
    }

    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        // initialize temporary point with current position
        Point newPosition = new Point(this.position);

        float movementSpeed = 0.12f;
        AnimationState animationState = AnimationState.IDLE;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newPosition.y += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newPosition.y -= movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            lookLeft = false;
            newPosition.x += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            lookLeft = true;
            newPosition.x -= movementSpeed;
        }
        if (level.isTileAccessible(newPosition)) {
            // calculate normalized delta of this.position and the calculated
            // new position to avoid increased diagonal movement speed
            var normalizedDelta = normalizeDelta(this.position, newPosition, movementSpeed);

            // is the hero moving?
            if (Math.abs(normalizedDelta.x) > 0.0f ||
                Math.abs(normalizedDelta.y) > 0.0f) {
                animationState = AnimationState.RUN;
            }

            this.position.x += normalizedDelta.x;
            this.position.y += normalizedDelta.y;
        }

        attackTargetIfReachable(this.position, level, game.getAllEntities());

        setCurrentAnimation(animationState);

        this.draw();
    }

    /**
     * Override IEntity.deletable and return false for the hero.
     * @return false
     */
    @Override
    public boolean deleteable() {
        return false;
    }

    private void resetCombatStats() {
        l.info("resetting combat stats");
        this.setHealth(maxHealth);
        this.canAttack = true;
    }
    /**
     * Set reference to DungeonWorld and spawn player at random position in the level.
     */
    public void setLevel(DungeonWorld level) {
        this.resetCombatStats();
        this.level = level;
        findRandomPosition();
    }

    /**
     * Sets the current position of the Hero to a random position inside the DungeonWorld.
     */
    public void findRandomPosition() {
        this.position = new Point(level.getRandomPointInDungeon());
    }
}