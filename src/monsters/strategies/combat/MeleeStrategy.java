package monsters.strategies.combat;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import util.math.Vec;

/** Strategy for melee combat. */
public class MeleeStrategy implements CombatStrategy {
  /** {@inheritDoc} */
  @Override
  public boolean rangeFunction(Point position, Point target) {
    return new Vec(position).subtract(new Vec(target)).magnitude() < 1f;
  }

  /** {@inheritDoc} */
  @Override
  public float getAttackValue() {
    return 20;
  }
}
