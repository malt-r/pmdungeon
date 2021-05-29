package monsters.strategies.combat;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import util.math.Vec;

public class MeleeStrategy implements CombatStrategy{

  @Override
  public boolean RangeFunction(Point position,Point target) {
    return new Vec(position).subtract(new Vec(target)).magnitude() < 1f;
  }

  @Override
  public float getAttackValue() {
    return 20;
  }
}
