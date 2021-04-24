package main.sample;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;
import main.ICombatable;

import java.util.*;
import java.util.logging.Logger;

public class MockMonster implements IAnimatable, IEntity, ICombatable {
    static Logger l = Logger.getLogger(MockMonster.class.getName());
    private final Animation idleAnimation;
    private Game game;
    private DungeonWorld level;
    private Point position;

    private final Timer attackTimer;
    private final long attackDelay;
    private boolean canAttack;
    private final Timer respawnTimer;
    private final long respawnDelay;

    public MockMonster(Game game) {
        this.game = game;

        String[] frames = new String[] {
                "tileset/chort_idle_anim_f0.png",
                "tileset/chort_idle_anim_f1.png",
                "tileset/chort_idle_anim_f2.png",
                "tileset/chort_idle_anim_f3.png"
        };
        this.idleAnimation = createAnimation(frames, 2);

        this.attackTimer = new Timer();
        this.attackDelay = 1000;
        this.respawnTimer = new Timer();
        this.respawnDelay = 500;
        this.canAttack = true;
    }

    private Animation createAnimation(String[] texturePaths, int frameTime)
    {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {
            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }

    @Override
    public Animation getActiveAnimation() {
        return idleAnimation;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void update() {
        attackTargetIfReachable(this.position, level, game.getAllEntities());
        if (!isDead()) {
            this.draw();
        }
    }

    @Override
    public boolean deleteable() {
        return false;
    }

    private void resetCombatStats() {
        l.info("resetting combat stats");
        this.setHealth(maxHealth);
        this.canAttack = true;
    }

    public void setLevel(DungeonWorld level) {
        this.resetCombatStats();
        this.level = level;
        findRandomPosition();
    }

    /**
     * Sets the current position of the Hero to a random position inside the DungeonWorld.
     */
    public void findRandomPosition() {
        l.info("I have spawned");
        this.position = new Point(level.getRandomPointInDungeon());
    }

    // ICombatible
    ICombatable target;

    float health = 100.f;
    float maxHealth = 100.f;
    float hitChance = 1.0f;
    float evasionChance = 0.0f;
    float damage = 10.f;

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
    public void heal(float amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    @Override
    public void dealDamage(float damage, ICombatable attacker) {
        ICombatable.super.dealDamage(damage, attacker);

        if (this.isDead()) {
            l.info("I am dead");

            TimerTask respawnTask = new TimerTask() {
                @Override
                public void run() {
                    findRandomPosition();
                    setHealth(maxHealth);
                }
            };
            respawnTimer.schedule(respawnTask, respawnDelay);
        }
    }

    @Override
    public float getHitChance() {
        return hitChance;
    }

    @Override
    public float getEvasionChance() {
        return evasionChance;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public boolean attack(ICombatable other) {
        boolean success = ICombatable.super.attack(other);

        this.canAttack = false;
        TimerTask attackResetTask = new TimerTask() {
            @Override
            public void run() {
                canAttack = true;
            }
        };
        this.attackTimer.schedule(attackResetTask, this.attackDelay);
        return success;
    }

    @Override
    public boolean canAttack() {
        return this.canAttack;
    }
}
