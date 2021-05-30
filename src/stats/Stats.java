package stats;

import java.util.HashMap;

public class Stats {
    HashMap<Attribute.AttributeType, Attribute> attributes = new HashMap();

    public Stats() {

    }

    public void addAttribute(Attribute attribute) {
        addAttribute(attribute, true);
    }

    public void addAttribute(Attribute attribute, boolean overwriteExistingOfSameType) {
        if (overwriteExistingOfSameType || !attributes.containsKey(attribute.getType())) {
            this.attributes.put(attribute.getType(), attribute);
        }
    }

    public void addInPlace(Attribute.AttributeType type, float value) {
        var attr = new Attribute(type, value);
        addAttribute(attr);
    }

    public float getValue(Attribute.AttributeType type) {
        if (this.attributes.containsKey(type)) {
            return this.attributes.get(type).getEffectiveValue();
        } else {
            return 0.f;
        }
    }

    public Attribute getAttribute(Attribute.AttributeType type) {
        return this.attributes.get(type);
    }

    public boolean applyModToAttribute(Modifier mod) {
        if (this.attributes.containsKey(mod.getTypeOfEffectedAttribute())) {
            return this.attributes.get(mod.getTypeOfEffectedAttribute()).applyModifier(mod);
        }
        return false;
    }

    public void removeAttributeOfType(Attribute.AttributeType type) {
        if (this.attributes.containsKey(type)) {
            this.attributes.remove(type);
        }
    }

    public void removeModifierFromAttribut(Modifier mod) {
        if (this.attributes.containsKey(mod.getTypeOfEffectedAttribute())) {
            var attribute = this.attributes.get(mod.getTypeOfEffectedAttribute());
            attribute.removeModifier(mod);
        }
    }

    public void clearModifiers() {
        for (var attr : this.attributes.values()) {
            attr.clearAllModifiers();
        }
    }
}
