package main;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.ArrayList;

/**
 * This interface implements the basic combat system.
 *
 * To work properly, the method attackTargetIfReachable needs to be called.
 * The default implementation of this method will scan for attackable entities and call the method setTarget
 * to cache a found target (a backing file for this cache needs to be provided by the implementing class).
 *
 * Required backing fields of the implementor:
 *
 * @author malte
 */
public interface ICombatable {
    /**
     * Used to get the health of the ICombatable.
     * A backing field needs to be provided.
     * @return The current health value.
     */
    float getHealth();

    /**
     * Used to set the healh of the ICombatable.
     * A backing field needs to be provided.
     * @param health The new value for the health backing field.
     */
    void setHealth(float health);

    /**
     * Used to increase the health value of the ICombatable by a passed amount.
     * @param amount The amount to increase the health value by.
     */
    void heal(float amount);

    /**
     * Used to determine if the ICombatable is dead.
     * A commond implementation is to check, if the health-value is 0.
     * @return A boolean indicating whether the implementor is dead.
     */
    default boolean isDead() {
        return getHealth() <= 0.f;
    }

    /**
     * Decrese the health-value of the ICombatable by the passed damage-amount.
     *
     * To set and get the current health value of the implementor, the methods setHealth and
     * getHealth are called.
     * This method will be called in the attack-method to deal damage on a successful hit.
     * @param damage The amount to decrease the health by.
     * @param attacker The ICombatable which deals the damage.
     */
    default void dealDamage(float damage, ICombatable attacker) {
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setHealth(0);
        }
    }

    /**
     * Used to check if the ICombatable has cached another ICombatable as a target.
     * @return A boolean, which indicates whether the implementor has already cached a target.
     */
    boolean hasTarget();

    /**
     * Used to get the cached ICombatable target, which should be attacked.
     * @return The cached target.
     */
    ICombatable getTarget();

    /**
     * Used to cache an ICombatable as a target in the implementor.
     * A backing field needs to be provided.
     * @param target The ICombatable to cache as a target.
     */
    void setTarget(ICombatable target);

    /**
     * Used to determine, if the ICombatable should be able to attack another ICombatable.
     *
     * This differs from canAttack in the way, that attackTargetIfInRange will not scan for attackable target, if
     * isPassive() returns true.
     *
     * @return A boolean indicating whether the ICombatable is passive.
     */
    boolean isPassive();

    /**
     * The main method of ICombatable, which will scan for attackable targets, cache the target and attack the target.
     *
     * This method should be called in the update-method of an IEntity. It calls the isPassive()-method in the beginning and
     * will switch off the searching for other ICombatables and attacking of ICombatables.
     *
     * If currently no ICombatable is cached as a target, it will search for an ICombatable which is located on the same tile
     * as provided by ownPosition and attack this target, if canAttack returns true.
     *
     * @param ownPosition The current position of the ICombatable. This is used to check, if other targets are in range for an attack.
     * @param level The current DungeonWorld in which the ICombatable exitst. This is used to get the tile, on which the ICombatable is located.
     * @param entities All other entities in the current DungeonWorld for which should be checked, if they are in range for an attack.
     */
    default void attackTargetIfReachable(Point ownPosition, DungeonWorld level, ArrayList<IEntity> entities) {
        if (!isPassive() && canAttack()) {
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

    /**
     * Checks, whether a given target is in range for an attack.
     *
     * It will compare the tiles on which the ownPosition and the position of target are located.
     * This requires the passed target to implement IDrawable!
     *
     * @param ownPosition The position of the ICombatable.
     * @param target The ICombatable for which should be checked, if it is in range for an attack.
     * @param level The current DungeonWorld in which the ICombatable exists. Used to get the tiles on which the target
     *              and the ownPosition are located.
     * @return
     */
    default boolean isTargetInRange(Point ownPosition, ICombatable target, DungeonWorld level) {
        int ownX = Math.round(ownPosition.x);
        int ownY = Math.round(ownPosition.y);
        var ownTile = level.getTileAt(ownX, ownY);
        if (target instanceof IDrawable) {
            Point otherPosition = ((IDrawable)target).getPosition();

            int otherX = Math.round(otherPosition.x);
            int otherY = Math.round(otherPosition.y);
            var otherTile = level.getTileAt(otherX, otherY);
            return ownTile == otherTile;
        }
        return false;
    }

    /**
     * Iterates over a passed list of IEntities and checks, if one of them is an ICombatable and in range for an attack.
     * @param ownPosition The position of the ICombatable.
     * @param entities An ArrayList of IEntity objects for which should be checked, if the are a suitable target.
     * @param level The level in which the ICombatable and the entities exist.
     * @return The found target. If no suitable target is found, null.
     */
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

    /**
     * Used to get the chance of a successfull attack of another ICombatable.
     *
     * A backing field needs to be provided.
     * @return The hit chance.
     */
    float getHitChance();

    /**
     * Used to get the chance to evade an attack by another ICombatable.
     * @return The evasion chance.
     */
    float getEvasionChance();

    /**
     * Used to get the damage value that should be dealt by the ICombatable.
     * @return The damage value.
     */
    float getDamage();

    /**
     * Used to check, if an attack can be performed by the ICombatable.
     * @return A boolean which indicates, whether an attack can be performed or not.
     */
    boolean canAttack();

    /**
     * Attack another ICombatable.
     *
     * This method uses getDamage() to determine the amount of damage, which should be dealt.
     * The getDamage()-method of the other ICombatable will be called to effectively subtract the damage-amount of it's health.
     * The hitChance()-method is used to calculate the chance of a successful hit.
     * @param other The ICombatable to attack.
     * @return If the attack was successful true, otherwise false.
     */
    default boolean attack(ICombatable other){
        // calculate hitChance
        float hitChance = getHitChance() * (1.f - other.getEvasionChance());

        float rand = (float)Math.random();
        if (rand < hitChance) {
            other.dealDamage(getDamage(), this);
            return true;
        } else {
            return false;
        }
    }
}