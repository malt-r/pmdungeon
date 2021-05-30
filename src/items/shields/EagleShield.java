package items.shields;

import items.IItemVisitor;
import stats.Attribute;
import stats.Modifier;

/**
 * Eagle Shield.
 *
 * <p>Contains everything that describes a Eagle Shield
 */
public class EagleShield extends Shield {
  /**
   * Constructor of the EagleShield class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public EagleShield() {
    super();
    this.modifiers.add(
        new Modifier(
            1.3f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.EVASION_CHANCE));
    String[] idleLeftFrames = new String[] {"tileset/items/shield_red_yellow_eagle.png"};
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Returns the name of the shield which can be used for display purposes
   *
   * @return Name of the shield
   */
  @Override
  public String getName() {
    return "The mighty red Shield";
  }

  /**
   * Returns the deescription of the shield which can be used for display purposes
   *
   * @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "Forgotten for centuries, one would say that only one of this kind of shield exists.";
  }

  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   *
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }
}
