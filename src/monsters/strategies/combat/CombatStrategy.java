package monsters.strategies.combat;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public interface CombatStrategy {
  boolean RangeFunction(Point position,Point target);
  float getAttackValue();
}
