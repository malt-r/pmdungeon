package items.weapons;

import items.IItemVisitor;
import stats.Attribute;
import stats.Modifier;

/**
 * Spear class.
 * <p>
 *   Contains everything that describes a spear.
 * </p>
 */
public class Spear extends Weapon{
  /**
   * Constructor of the Spear class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Spear() {
    super();
    this.modifiers.add(new Modifier(1.5f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.PHYSICAL_ATTACK_DAMAGE));
    this.modifiers.add(new Modifier(0.7f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.HIT_CHANCE));
    this.range=1.0f;
    this.condition=100;

    String[] idleLeftFrames = new String[]{
            "tileset/items/weapon_spear.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
  /**
   * Returns the name of the spear for display purposes
   * @return Name of the spear
   */
  @Override
  public String getName() {
    return "Spear";
  }
  /**
   * description of the spear for display purposes
   * @return description of the spear
   */
  @Override
  protected String getDescription() {
    return "Pointy Spear";
  }
  /**
   * Called each frame and draws the regular sword with the right scaling.
   */
  @Override
  public void update(){
    draw(-0.60f,-1f,0.25f,1.0f);
  }
  /**
   * Accept method for a item visitor to extend the functionality of the spear class.
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
