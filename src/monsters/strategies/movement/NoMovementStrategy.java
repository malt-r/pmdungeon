package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/** Movement strategy where the entity does not move. */
public class NoMovementStrategy implements MovementStrategy {
  /** {@inheritDoc} */
  @Override
  public Point Move(Point currentPosition, DungeonWorld level) {
    return currentPosition;
  }
}
