package mock;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import items.Item;

public class MockItem extends Item {

  @Override
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    return null;
  }

  @Override
  public String getName() {
    return "mockitem";
  }

  @Override
  protected String getDescription() {
    return "mockitem";
  }
}
