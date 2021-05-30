package stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A stat-attribute. Stores a base value and modifiers, which will be either multiplied or added to
 * the base value to calculate the effective attribute value.
 */
public class Attribute {

  // types of attributes, which's effective value can be set directly
  private static final HashSet<AttributeType> directlySettableAttributes =
      Stream.of(AttributeType.HEALTH).collect(Collectors.toCollection(HashSet::new));
  private final float baseValue;
  private final AttributeType type;
  private final ArrayList<Modifier> multipliers = new ArrayList<>();
  private final ArrayList<Modifier> adders = new ArrayList<>();
  private float effectiveValue;
  /**
   * Constructor.
   *
   * @param type The type of the attribute.
   * @param baseValue The basevalue of the attribute.
   */
  public Attribute(AttributeType type, float baseValue) {
    this.type = type;
    this.baseValue = baseValue;
    this.effectiveValue = baseValue;
  }

  /**
   * Gets the type.
   *
   * @return the type.
   */
  public AttributeType getType() {
    return type;
  }

  /**
   * Apply a modifier to this attribute. Prevents double adding.
   *
   * @param mod The modifier to apply.
   * @return True if successfull.
   */
  public boolean applyModifier(Modifier mod) {
    if (mod.getTypeOfEffectedAttribute() != type) {
      return false;
    } else if (mod.getType() == Modifier.ModifierType.MULTIPLIER && !multipliers.contains(mod)) {
      multipliers.add(mod);
      recalculateEffectiveValue();
      return true;
    } else if (mod.getType() == Modifier.ModifierType.ADDITION && !adders.contains(mod)) {
      adders.add(mod);
      recalculateEffectiveValue();
      return true;
    }
    return false;
  }

  /**
   * Remove a modifier from this attribute.
   *
   * @param mod The modifier to remove.
   * @return True if successfull.
   */
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

  // recalculate effective value of attribute with multipliers and adders.
  private void recalculateEffectiveValue() {
    float modifiedBaseValue = this.baseValue;
    for (var mod : adders) {
      modifiedBaseValue += mod.getValue();
    }

    float effectiveMultiplier = 1.0f;
    for (var mod : multipliers) {
      effectiveMultiplier *= mod.getValue();
    }
    this.effectiveValue = modifiedBaseValue * effectiveMultiplier;
  }

  /**
   * get the effective Value of this attribute.
   *
   * @return The effective value.
   */
  public float getEffectiveValue() {
    return effectiveValue;
  }

  /**
   * Sets the effective Value of this attribute. Only applicable for Type HEALTH.
   *
   * @param value The new effective Value.
   * @return True is successfull.
   */
  public boolean setEffectiveValue(float value) {
    if (directlySettableAttributes.contains(this.type)) {
      this.effectiveValue = value;
    }
    return false;
  }

  /** Remove all modifiers from this attribute. */
  public void clearAllModifiers() {
    this.multipliers.clear();
    this.adders.clear();
    if (directlySettableAttributes.contains(this.type)) {
      this.effectiveValue = this.baseValue;
    }
  }

  /** The type of attributes. */
  public enum AttributeType {
    HEALTH,
    MAX_HEALTH,
    MOVEMENT_SPEED,
    PHYSICAL_ATTACK_DAMAGE,
    HIT_CHANCE,
    EVASION_CHANCE
  }
}
