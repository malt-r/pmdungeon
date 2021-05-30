package main;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This interface implements the basic combat system.
 *
 * <p>To work properly, the method attackTargetIfReachable needs to be called. The default
 * implementation of this method will scan for attackable entities and call the method setTarget to
 * cache a found target (a backing file for this cache needs to be provided by the implementing
 * class).
 *
 * <p>Required backing fields of the implementor:
 *
 * @author malte
 */
public interface ICombatable {
  /**
   * Used to get the health of the ICombatable. A backing field needs to be provided.
   *
   * @return The current health value.
   */
  float getHealth();

  /**
   * Used to set the healh of the ICombatable. A backing field needs to be provided.
   *
   * @param health The new value for the health backing field.
   */
  void setHealth(float health);

  /**
   * Used to increase the health value of the ICombatable by a passed amount.
   *
   * @param amount The amount to increase the health value by.
   */
  void heal(float amount);

  /**
   * Used to determine if the ICombatable is dead. A commond implementation is to check, if the
   * health-value is 0.
   *
   * @return A boolean indicating whether the implementor is dead.
   */
  default boolean isDead() {
    return getHealth() <= 0.f;
  }

  /**
   * Decrese the health-value of the ICombatable by the passed damage-amount.
   *
   * <p>To set and get the current health value of the implementor, the methods setHealth and
   * getHealth are called. This method will be called in the attack-method to deal damage on a
   * successful hit.
   *
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
   *
   * @return A boolean, which indicates whether the implementor has already cached a target.
   */
  boolean hasTarget();

  /**
   * Used to get the cached ICombatable target, which should be attacked.
   *
   * @return The cached target.
   */
  ICombatable getTarget();

  /**
   * Used to cache an ICombatable as a target in the implementor. A backing field needs to be
   * provided.
   *
   * @param target The ICombatable to cache as a target.
   */
  void setTarget(ICombatable target);

  /**
   * Check, if the other ICombatable should be attacked or is friendly
   *
   * @param other The other ICombatable.
   * @return True, if the other ICombatable should not be attacked.
   */
  boolean isOtherFriendly(ICombatable other);

  /**
   * Used to determine, if the ICombatable should be able to attack another ICombatable.
   *
   * <p>This differs from canAttack in the way, that attackTargetIfInRange will not scan for
   * attackable target, if isPassive() returns true.
   *
   * @return A boolean indicating whether the ICombatable is passive.
   */
  boolean isPassive();

  /**
   * The main method of ICombatable, which will scan for attackable targets, cache the target and
   * attack the target.
   *
   * <p>This method should be called in the update-method of an IEntity. It calls the
   * isPassive()-method in the beginning and will switch off the searching for other ICombatables
   * and attacking of ICombatables.
   *
   * <p>If currently no ICombatable is cached as a target, it will search for an ICombatable which
   * is located on the same tile as provided by ownPosition and attack this target, if canAttack
   * returns true.
   *
   * @param ownPosition The current position of the ICombatable. This is used to check, if other
   *     targets are in range for an attack.
   * @param inRangeFunc Function which determines if a given point is in range of this ICombatable.
   *     The passed Point will be the position of a potential target.
   * @param entityFinder
   */
  default void attackTargetIfReachable(
      Point ownPosition,
      Function<Point, Boolean> inRangeFunc,
      BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder) {
    if (!isTargetInRange(getTarget(), inRangeFunc)) {
      setTarget(null);
    }
    if (!isPassive() && canAttack()) {
      if (hasTarget() && attackOnInput()) {
        attack(getTarget());
      } else {
        setTarget(findTarget(ownPosition, entityFinder, inRangeFunc));
        if (hasTarget() && attackOnInput()) {
          attack(getTarget());
        }
      }
    }
  }

  // TODO: remove? This is the legacy way of doing this..
  default void attackTargetIfReachable(
      Point ownPosition, DungeonWorld level, ArrayList<IEntity> entities) {
    Function<Point, Boolean> inRangeFunc =
        (p) -> {
          var ownTile = level.getTileAt((int) ownPosition.x, (int) ownPosition.y);
          var tileOfP = level.getTileAt((int) p.x, (int) p.y);
          return ownTile == tileOfP;
        };

    BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder =
        (p1, p2) -> {
          ArrayList<DrawableEntity> drawableEntities = new ArrayList<>();
          for (var entity : entities) {
            if (entity instanceof DrawableEntity) {
              drawableEntities.add((DrawableEntity) entity);
            }
          }
          return drawableEntities;
        };

    attackTargetIfReachable(ownPosition, inRangeFunc, entityFinder);
  }

  /**
   * Checks, if a given ICombatable target is in range for an attack.
   *
   * @param target the target to check for potential attack.
   * @param isInRangeFunc The function, which will be used to determine, if the target is in range.
   *     The position of the potential target will be passed as a parameter to this function.
   * @return True, if the target is in range, false otherwise.
   */
  default boolean isTargetInRange(ICombatable target, Function<Point, Boolean> isInRangeFunc) {
    if (target instanceof IDrawable) {
      Point otherPosition = ((IDrawable) target).getPosition();

      int otherX = Math.round(otherPosition.x);
      int otherY = Math.round(otherPosition.y);
      Point roundedOtherPos = new Point(otherX, otherY);
      return isInRangeFunc.apply(roundedOtherPos);
    }
    return false;
  }

  /**
   * Together with lowerAttackRangeBound, this function creates a rectangle in which entities will
   * be checked as potential targets. This function returns the lower bound of this rectangle (lower
   * bound of x and y coordinates).
   *
   * @param ownPosition The position from which the rectangle will be created.
   * @return The lower bound of the calculated rectangle.
   */
  default Point lowerAttackRangeBound(Point ownPosition) {
    return new Point((float) Math.floor(ownPosition.x), (float) Math.floor(ownPosition.y));
  }

  /**
   * Together with lowerAttackRangeBound, this function creates a rectangle in which entities will
   * be checked as potential targets. This function returns the upper bound of this rectangle (upper
   * bound of x and y coordinates).
   *
   * @param ownPosition The position from which the rectangle will be created.
   * @return The upper bound of the calculated rectangle.
   */
  default Point upperAttackRangeBound(Point ownPosition) {
    return new Point((float) Math.ceil(ownPosition.x) + 1, (float) Math.ceil(ownPosition.y) + 1);
  }

  /**
   * Iterates over a passed list of IEntities and checks, if one of them is an ICombatable and in
   * range for an attack.
   *
   * @param ownPosition The position of the ICombatable. //* @param entities An ArrayList of IEntity
   *     objects for which should be checked, if the are a suitable target.
   * @param inRangeFunc The function, which will determine, if the position of a potential target is
   *     in range. The passed Point will be the position of a potential target.
   * @param entityFinder The function, which will return the DrawableEntities, for which will be
   *     checked, if they are a potential target.
   * @return The found target. If no suitable target is found, null.
   */
  default ICombatable findTarget(
      Point ownPosition,
      BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder,
      Function<Point, Boolean> inRangeFunc) {
    var lowerBound = lowerAttackRangeBound(ownPosition);
    var upperBound = upperAttackRangeBound(ownPosition);

    ArrayList<DrawableEntity> entities = entityFinder.apply(lowerBound, upperBound);
    entities.remove(this);
    if (this instanceof Hero && entities.size() > 0) {
      if (entities.contains(this)) {
        ArrayList<DrawableEntity> testEntities = entityFinder.apply(lowerBound, upperBound);
      }
    }

    // Todo - find nearest target
    for (DrawableEntity entity : entities) {
      if (!entity.equals(this) && entity instanceof ICombatable) {
        var combatable = (ICombatable) entity;
        if (!isOtherFriendly(combatable)) {
          // is in range/on the same tile?
          if (isTargetInRange(combatable, inRangeFunc)) {
            return combatable;
          }
        }
      }
    }
    return null;
  }

  /**
   * Used to get the chance of a successfull attack of another ICombatable.
   *
   * <p>A backing field needs to be provided.
   *
   * @return The hit chance.
   */
  float getHitChance();

  /**
   * Used to get the chance to evade an attack by another ICombatable.
   *
   * @return The evasion chance.
   */
  float getEvasionChance();

  /**
   * Used to get the damage value that should be dealt by the ICombatable.
   *
   * @return The damage value.
   */
  float getDamage();

  /**
   * Used to check, if an attack can be performed by the ICombatable.
   *
   * @return A boolean which indicates, whether an attack can be performed or not.
   */
  default boolean canAttack() {
    return !isDead();
  }

  /**
   * Attack another ICombatable.
   *
   * <p>This method uses getDamage() to determine the amount of damage, which should be dealt. The
   * getDamage()-method of the other ICombatable will be called to effectively subtract the
   * damage-amount of it's health. The hitChance()-method is used to calculate the chance of a
   * successful hit.
   *
   * @param other The ICombatable to attack.
   * @return If the attack was successful true, otherwise false.
   */
  default float attack(ICombatable other) {
    // calculate hitChance
    float hitChance = getHitChance() * (1.f - other.getEvasionChance());

    float rand = (float) Math.random();
    if (rand < hitChance) {
      float damage = getDamage();
      other.dealDamage(damage, this);
      return damage;
    } else {
      return 0.0f;
    }
  }

  default boolean attackOnInput() {
    return true;
  }
}
