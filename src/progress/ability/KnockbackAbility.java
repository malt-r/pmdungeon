package progress.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import main.Actor;
import progress.effect.KnockBackEffect;
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

    @Override
    public void activate(Actor origin) {
        //check for area around self and initiate knockback
        var originPostion = origin.getPosition();
        var entities = main.Game.getInstance().getAllEntities();
        ArrayList<Actor> actorsToKnockBack = new ArrayList<>();
        for (IEntity entity : entities) {
            if (entity.equals(origin)) {
                continue;
            }
            if (entity instanceof Actor) {
                var actor = (Actor)entity;
                var positon = actor.getPosition();
                var diff = new util.math.Vec(positon).subtract(new Vec(originPostion)).magnitude();
                if (diff <= areaRadius) {
                    actorsToKnockBack.add(actor);
                }
            }
        }

        var effect = new KnockBackEffect(originPostion);
        for (Actor actor : actorsToKnockBack) {
            actor.applyOneShotEffect(effect);
        }
    }
}
