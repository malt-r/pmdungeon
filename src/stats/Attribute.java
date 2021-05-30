package stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Attribute {
    public enum AttributeType {
        HEALTH,
        MAX_HEALTH,
        MOVEMENT_SPEED,
        PHYSICAL_ATTACK_DAMAGE,
        HIT_CHANCE,
        EVASION_CHANCE
    }

    private static HashSet<AttributeType> directlySettableAttributes =
            Stream.of(AttributeType.HEALTH)
            .collect(Collectors.toCollection(HashSet::new));

    private final float baseValue;
    private float effectiveValue;
    private AttributeType type;

    private ArrayList<Modifier> multipliers = new ArrayList<>();
    private ArrayList<Modifier> adders = new ArrayList<>();

    public Attribute(AttributeType type, float baseValue) {
        this.type = type;
        this.baseValue = baseValue;
        this.effectiveValue = baseValue;
    }

    public AttributeType getType() {
        return type;
    }

    public boolean applyModifier(Modifier mod) {
        if (mod.getTypeOfEffectedAttribute() != type) {
            return false;
        } else if (mod.getType() == Modifier.ModifierType.MULTIPLIER && !multipliers.contains(mod)) {
            multipliers.add(mod);
            System.out.println("APPLY MODIFIER");
            recalculateEffectiveValue();
            return true;
        } else if (mod.getType() == Modifier.ModifierType.ADDITION && !adders.contains(mod)) {
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

    public boolean setEffectiveValue(float value) {
        if (directlySettableAttributes.contains(this.type)) {
            this.effectiveValue = value;
        }
        return false;
    }

    public void clearAllModifiers() {
        this.multipliers.clear();
        this.adders.clear();
        if (directlySettableAttributes.contains(this.type)) {
            this.effectiveValue = this.baseValue;
        }
    }
}
