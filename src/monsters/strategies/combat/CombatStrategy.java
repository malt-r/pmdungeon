package monsters.strategies.combat;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * Interface for defining combat strategies that can be used by monsters
 */
public interface CombatStrategy {
  /**
   * Calculates if the Target position is in range.
   * @param position own position of the monster
   * @param target target position
   * @return if the target point is in range.
   */
  boolean RangeFunction(Point position,Point target);

  /**
   * Gets the Attackvalue of this strategy.
   * @return the attack value.
   */
  float getAttackValue();
}
