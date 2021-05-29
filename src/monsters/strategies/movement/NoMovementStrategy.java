package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class NoMovementStrategy implements MovementStrategy{
  @Override
  public Point Move(Point currentPosition, DungeonWorld level) {
    return currentPosition;
  }
}
