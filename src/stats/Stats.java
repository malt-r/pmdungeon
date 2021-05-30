package stats;

import java.util.HashMap;

/**
 * Collection of stats.
 */
public class Stats {
    /**
     * the stored attributes.
     */
    protected HashMap<Attribute.AttributeType, Attribute> attributes = new HashMap();

    /**
     * add another attribute. Will overwrite an existing attribute.
     * @param attribute the attribute to add.
     */
    public void addAttribute(Attribute attribute) {
        addAttribute(attribute, true);
    }

    /**
     * Add another attribute and optionally overwrite an existing attribute.
     * @param attribute The attribute to add.
     * @param overwriteExistingOfSameType Overwrite?
     */
    public void addAttribute(Attribute attribute, boolean overwriteExistingOfSameType) {
        if (overwriteExistingOfSameType || !attributes.containsKey(attribute.getType())) {
            this.attributes.put(attribute.getType(), attribute);
        }
    }

    /**
     * Add another attribute of type with value. Overwrites existing attribute.
     * @param type The type of the new attribute.
     * @param value The value of the new attribute.
     */
    public void addInPlace(Attribute.AttributeType type, float value) {
        var attr = new Attribute(type, value);
        addAttribute(attr);
    }

    /**
     * Gets the effective value of an attribute.
     * @param type The type of attribute to get the value for.
     * @return The effective value of the specified attribute.
     */
    public float getValue(Attribute.AttributeType type) {
        if (this.attributes.containsKey(type)) {
            return this.attributes.get(type).getEffectiveValue();
        } else {
            return 0.f;
        }
    }

    /**
     * Returns the specified attribute.
     * @param type
     * @return
     */
    public Attribute getAttribute(Attribute.AttributeType type) {
        return this.attributes.get(type);
    }

    /**
     * Applies a modified to the attributes specified by the TypeOfEffectedAttribute from mod.
     * @param mod The modifier to apply.
     * @return True if successfull. False otherwise.
     */
    public boolean applyModToAttribute(Modifier mod) {
        if (this.attributes.containsKey(mod.getTypeOfEffectedAttribute())) {
            return this.attributes.get(mod.getTypeOfEffectedAttribute()).applyModifier(mod);
        }
        return false;
    }

    /**
     * Remove a modifier from an attribute.
     * @param mod The modifier to remove.
     */
    public void removeModifierFromAttribut(Modifier mod) {
        if (this.attributes.containsKey(mod.getTypeOfEffectedAttribute())) {
            var attribute = this.attributes.get(mod.getTypeOfEffectedAttribute());
            attribute.removeModifier(mod);
        }
    }

    /**
     * Remove all modifiers from all attributes.
     */
    public void clearModifiers() {
        for (var attr : this.attributes.values()) {
            attr.clearAllModifiers();
        }
    }
}
