import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.ArrayList;

public interface ICombatable {
    float getHealth();
    void setHealth(float health);
    void heal(float amount);
    boolean isDead();
    default void dealDamage(float damage) {
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setHealth(0);
        }
    }

    boolean hasTarget();
    ICombatable getTarget();
    void setTarget(ICombatable target);

    boolean isPassive();

    default void attackTargetIfReachable(Point ownPosition, DungeonWorld level, ArrayList<IEntity> entities) {
        if (!isPassive()) {
            if (hasTarget()) {
                if (!isTargetInRange(ownPosition, getTarget(), level)) {
                    setTarget(null);
                } else {
                    attack(getTarget());
                }
            } else {
                setTarget(findTarget(ownPosition, entities, level));
                if (hasTarget()) {
                    attack(getTarget());
                }
            }
        }
    }

    default boolean isTargetInRange(Point ownPosition, ICombatable target, DungeonWorld level) {
        var ownTile = level.getTileAt((int)ownPosition.x, (int)ownPosition.y);
        if (target instanceof IDrawable) {
            Point otherPosition = ((IDrawable)target).getPosition();

            var otherTile = level.getTileAt((int)otherPosition.x, (int)otherPosition.y);
            if (ownTile == otherTile) {
                return true;
            }
        }
        return false;
    }

    default ICombatable findTarget(Point ownPosition, ArrayList<IEntity> entities, DungeonWorld level) {
        for (IEntity entity : entities) {
            if (!entity.equals(this) && entity instanceof ICombatable) {
                var combatable = (ICombatable) entity;
                // is in range/on the same tile?
                if (isTargetInRange(ownPosition, combatable, level)) {
                    return combatable;
                }
            }
        }
        return null;
    }

    // maybe depending on equipped weapon, potion, status effect...
    float getHitChance();

    // dependent of monstertype, equipment, etc.
    float getEvasionChance();

    // maybe depending on equipped weapon, potion, status effect...
    float getDamage();

    boolean canAttack();
    // attack other attackable
    // calculate success chance based on own stats and stats of other (may be modified by items)
    default void attack(ICombatable other){
        if (canAttack()) {
            // calculate hitChance
            float hitChance = getHitChance() * (1.f - other.getEvasionChance());

            float rand = (float)Math.random();
            if (rand < hitChance) {
                other.dealDamage(getDamage());
            }
        }
    }
}