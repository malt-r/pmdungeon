package stats;

/**
 * A modifier which can be applied to an attribute.
 */
public class Modifier {

    /**
     * Type of the modification (either Multiplication or Addition).
     */
    public enum ModifierType {
        MULTIPLIER,
        ADDITION
    }

    // the type of the effected attribute
    private Attribute.AttributeType typeOfEffectedAttribute;
    private ModifierType type;
    private float value;

    /**
     * Constructor.
     * @param value The value of the modification.
     * @param type The type of the modification.
     * @param typeOfEffectedAttribute The effected attribute type.
     */
    public Modifier(float value, ModifierType type, Attribute.AttributeType typeOfEffectedAttribute) {
        this.value = value;
        this.type = type;
        this.typeOfEffectedAttribute = typeOfEffectedAttribute;
    }

    /**
     * Gets the value.
     * @return The value.
     */
    public float getValue() {
        return value;
    }

    /**
     * Gets the type of effected attribute.
     * @return The type of effected attribute.
     */
    public Attribute.AttributeType getTypeOfEffectedAttribute() {
        return typeOfEffectedAttribute;
    }

    /**
     * Gets the type of modification.
     * @return The type of modification.
     */
    public ModifierType getType() {
        return type;
    }

}
