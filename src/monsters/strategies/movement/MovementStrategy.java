package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public interface MovementStrategy {
  Point Move(Point currentPosition, DungeonWorld level);
}
