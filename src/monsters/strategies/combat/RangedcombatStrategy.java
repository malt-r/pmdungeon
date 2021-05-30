package monsters.strategies.combat;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import util.math.Vec;

/** Combat strategy for distance attacks. */
public class RangedcombatStrategy implements CombatStrategy {

  /** {@inheritDoc} */
  @Override
  public boolean RangeFunction(Point position, Point target) {
    return new Vec(position).subtract(new Vec(target)).magnitude() < 5f;
  }

  /** {@inheritDoc} */
  @Override
  public float getAttackValue() {
    return 10;
  }
}
