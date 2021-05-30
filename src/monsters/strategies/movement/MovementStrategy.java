package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/** Interface for defining Movement stragies */
public interface MovementStrategy {
  /**
   * The strategy returns a Point where the entities should be move forward
   *
   * @param currentPosition position of the entity
   * @param level levle of the entity
   * @return a target point for the entity.
   */
  Point Move(Point currentPosition, DungeonWorld level);
}
