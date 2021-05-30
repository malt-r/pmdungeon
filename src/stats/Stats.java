package stats;

import java.util.HashMap;

public class Stats {
    HashMap<Attribute.AttributeType, Attribute> attributes = new HashMap();

    public Stats() {

    }

    public void addAttribute(Attribute attribute, boolean overwriteExistingOfSameType) {
        if (overwriteExistingOfSameType || !attributes.containsKey(attribute.getType())) {
            this.attributes.put(attribute.getType(), attribute);
        }
    }

    public float getEffectiveAttributeValue(Attribute.AttributeType type) {
        if (this.attributes.containsKey(type)) {
            return this.attributes.get(type).getEffectiveValue();
        } else {
            return 0.f;
        }
    }

    public Attribute getAttributeOfType(Attribute.AttributeType type) {
        return this.attributes.get(type);
    }

    public void removeAttributeOfType(Attribute.AttributeType type) {
        if (this.attributes.containsKey(type)) {
            this.attributes.remove(type);
        }
    }
}
