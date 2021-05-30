package stats;

import java.util.ArrayList;

public class Attribute {
    enum AttributeType {
        HEALTH,
        MAX_HEALTH,
        MOVEMENT_SPEED,
        PHYSICAL_ATTACK_DAMAGE,
        HIT_CHANCE,
        EVASION_CHANCE
    }

    private final float baseValue;
    private float effectiveValue;
    private AttributeType type;

    private ArrayList<Modifier> multipliers;
    private ArrayList<Modifier> adders;

    public Attribute(AttributeType type, float baseValue) {
        this.type = type;
        this.baseValue = baseValue;
    }

    public AttributeType getType() {
        return type;
    }

    public boolean applyModifier(Modifier mod) {
        if (mod.getTypeOfEffectedAttribute() != type) {
            return false;
        } else if (mod.getType() == Modifier.ModifierType.MULTIPLIER) {
            multipliers.add(mod);
            recalculateEffectiveValue();
            return true;
        } else if (mod.getType() == Modifier.ModifierType.ADDITION) {
            adders.add(mod);
            recalculateEffectiveValue();
            return true;
        }
        return false;
    }

    public boolean removeModifier(Modifier mod) {
        if (mod.getTypeOfEffectedAttribute() != type) {
            return false;
        } else if (mod.getType() == Modifier.ModifierType.MULTIPLIER) {
            multipliers.remove(mod);
            recalculateEffectiveValue();
            return true;
        } else if (mod.getType() == Modifier.ModifierType.ADDITION) {
            adders.remove(mod);
            recalculateEffectiveValue();
            return true;
        }
        return false;
    }

    private void recalculateEffectiveValue() {
        float modifiedBaseValue = this.baseValue;
        for (var mod : adders) {
            modifiedBaseValue += mod.getValue();
        }

        float effectiveMultiplier = 1.0f;
        for (var mod : multipliers) {
            effectiveMultiplier *= mod.getValue();
        }
        this.effectiveValue =  modifiedBaseValue * effectiveMultiplier;
    }

    public float getEffectiveValue() {
        return effectiveValue;
    }
}
