package mock;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import monsters.Monster;

public class MockMonster extends Monster {
  public MockMonster(Point position) {
    this.setPosition(position);
  }

  @Override
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    return null;
  }
}
