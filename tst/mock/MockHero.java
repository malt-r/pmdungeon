package mock;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Hero;

import java.util.ArrayList;

public class MockHero extends Hero {

  public MockHero() {}

  public MockHero(Point position) {
    this.setPosition(position);
  }

  @Override
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    return null;
  }

  @Override
  public void attackTargetIfReachable(
      Point ownPosition, DungeonWorld level, ArrayList<IEntity> entities) {
    super.attackTargetIfReachable(ownPosition, level, entities);
  }

  @Override
  public boolean attackOnInput() {
    return true;
  }
}
