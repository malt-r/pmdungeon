package items.shields;

import items.IItemVisitor;
import stats.Attribute;
import stats.Modifier;

/**
 * Wood Shield.
 * <p>
 *   Contains everything that describes a Wood shield.
 * </p>
 */
public class WoodShield extends Shield{
  /**
   * Constructor of the Woodshield class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public WoodShield() {
    super();
    this.modifiers.add(new Modifier(1.1f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.EVASION_CHANCE));
    String[] idleLeftFrames = new String[]{
            "tileset/items/shield_wood.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
  /**
   *  Returns the name of the shield which can be used for display purposes
   *  @return Name of the shield
   */
  @Override
  public String getName() {
    return "Wooden Shield";
  }
  /**
   *  Returns the deescription of the shield which can be used for display purposes
   *  @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "A simple shield. Better than a sheet of paper.";
  }
  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
