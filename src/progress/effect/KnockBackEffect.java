package progress.effect;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;

/**
 * One-shot effect to initiate a knock back of an actor.
 * @author malte
 */
public class KnockBackEffect extends OneShotEffect {
    float magnitude = 3.f;
    Point originPoint;

    public KnockBackEffect(Point originPoint) {
        this.originPoint = originPoint;
    }

    @Override
    public void applyTo(Actor target) {
        target.initiateKnockBackFromPoint(originPoint, magnitude);
    }
}
