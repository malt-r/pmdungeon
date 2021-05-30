package stats;

import main.Actor;
import main.DrawableEntity;
import progress.effect.PersistentEffect;

import java.util.concurrent.Callable;

public class Modifier extends PersistentEffect {
    public enum ModifierType {
        MULTIPLIER,
        ADDITION
    }

    private Attribute.AttributeType typeOfEffectedAttribute;
    private ModifierType type;
    private float value;

    public Modifier(float value, ModifierType type, Attribute.AttributeType typeOfEffectedAttribute) {
        this.value = value;
        this.type = type;
        this.typeOfEffectedAttribute = typeOfEffectedAttribute;
    }

    public float getValue() {
        return value;
    }

    public Attribute.AttributeType getTypeOfEffectedAttribute() {
        return typeOfEffectedAttribute;
    }

    public ModifierType getType() {
        return type;
    }

    @Override
    public void onApply(Actor target) {

    }

    @Override
    protected Callable<Boolean> getRemovalCheck() {
        return null;
    }

    @Override
    public void onRemoval(Actor target) {

    }
}
