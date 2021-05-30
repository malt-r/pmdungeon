package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;
import util.math.Convenience;

/** This Strategy follows a hero to a specific range. */
public class FollowHeroStrategy implements MovementStrategy {

  /** {@inheritDoc} */
  @Override
  public Point Move(Point currentPosition, DungeonWorld level) {

    int startX = (int) currentPosition.x;
    int startY = (int) currentPosition.y;

    int endX = (int) Game.getInstance().getHero().getPosition().x;
    int endY = (int) Game.getInstance().getHero().getPosition().y;

    var startTile = (level.getTileAt(startX, startY));
    var endTile = (level.getTileAt(endX, endY));
    var res = level.findPath(startTile, endTile);

    if (res.getCount() > 2) {
      var resX = res.get(1).getX();
      var resY = res.get(1).getY();
      return new Point(resX, resY);
    }

    if (!Convenience.checkForIntersection(
        currentPosition, Game.getInstance().getHero().getPosition(), 0.2f)) {
      return Game.getInstance().getHero().getPosition();
    }
    return currentPosition;
  }
}
