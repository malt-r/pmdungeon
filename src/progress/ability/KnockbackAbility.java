package progress.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.Game;
import progress.effect.KnockBackEffect;
import util.math.Convenience;
import util.math.Vec;
import progress.ability.Ability;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Ability to knock back other Actors in an area of effect.
 * @author malte
 */
public class KnockbackAbility extends Ability {
    float areaRadius = 5.f;
    private Callable<Boolean> activationCheck;

    /**
     * constructor.
     * @param activationCheck Callable, which returns true, if the ability should be activated.
     */
    public KnockbackAbility(Callable<Boolean> activationCheck) {
        this.activationCheck = activationCheck;
    }

    @Override
    protected Callable<Boolean> getActivationCheck() {
        return activationCheck;
    }


    protected Point calcLowerAreaBound(Point center) {
        return new Vec(Convenience.getFlooredPoint(center)).subtract(new Vec(areaRadius / 2, areaRadius / 2)).toPoint();
    }

    protected Point calcUpperAreaBound(Point center) {
        return new Vec(center).add(new Vec(areaRadius / 2, areaRadius / 2)).toPoint();
    }

    @Override
    public void activate(Actor origin) {
        //check for area around self and initiate knockback
        var originPosition = origin.getPosition();

        var lowerBound = calcLowerAreaBound(originPosition);
        var upperBound = calcUpperAreaBound(originPosition);
        var entities = Game.getInstance().getEntitiesInCoordRange(lowerBound, upperBound);

        ArrayList<Actor> actorsToKnockBack = new ArrayList<>();
        for (IEntity entity : entities) {
            if (entity.equals(origin)) {
                continue;
            }
            if (entity instanceof Actor) {
                var actor = (Actor)entity;
                var positon = actor.getPosition();
                var diff = new util.math.Vec(positon).subtract(new Vec(originPosition)).magnitude();
                if (diff <= areaRadius) {
                    actorsToKnockBack.add(actor);
                }
            }
        }

        var effect = new KnockBackEffect(originPosition);
        for (Actor actor : actorsToKnockBack) {
            actor.applyOneShotEffect(effect);
        }
    }
}
