package items.weapons;

import items.IItemVisitor;
import stats.Attribute;
import stats.Modifier;

/**
 * Regular sword class.
 *
 * <p>Contains everything that describes a regular sword.
 */
public class RegularSword extends Weapon {
  /**
   * Constructor of the RegularWord class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public RegularSword() {
    super();
    this.modifiers.add(
        new Modifier(
            1.3f,
            Modifier.ModifierType.MULTIPLIER,
            Attribute.AttributeType.PHYSICAL_ATTACK_DAMAGE));
    this.modifiers.add(
        new Modifier(1.4f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.HIT_CHANCE));
    this.range = 1.0f;
    this.condition = 100;
    String[] idleLeftFrames = new String[] {"tileset/items/weapon_regular_sword.png"};
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Returns the name of the sword for display purposes.
   *
   * @return Name of the swords
   */
  @Override
  public String getName() {
    return "Regular Sword";
  }

  /**
   * description of the sword for display purposes.
   *
   * @return description of the regular sword
   */
  @Override
  protected String getDescription() {
    return "Thicc sword";
  }

  /** Called each frame and draws the regular sword with the right scaling. */
  @Override
  public void update() {
    draw(-0.75f, -0.75f, 0.5f, 1.0f);
  }

  /**
   * Accept method for a item visitor to extend the functionality of the regular sword class.
   *
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }
}
