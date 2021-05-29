package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;

import static main.DrawableEntity.game;

public class FollowHeroStrategy implements MovementStrategy{
  public FollowHeroStrategy(){
  }

  @Override
  public Point Move(Point currentPosition, DungeonWorld level) {

    int startX = (int) currentPosition.x;
    int startY = (int) currentPosition.y;

    int endX = (int) Game.getInstance().getHero().getPosition().x;
    int endY = (int)Game.getInstance().getHero().getPosition().y;

    var startTile = (level.getTileAt(startX,startY));
    var endTile = (level.getTileAt(endX,endY));
    var res = level.findPath(startTile,endTile);


    if(res.getCount()>1 && res.getCount()<5){
    var resX = res.get(1).getX();
    var resY = res.get(1).getY();
    if(Math.abs(startX-endX)+Math.abs(startY-endY)>1) {
      return new Point(resX, resY);
    }
    }

    return currentPosition;
  }
}
