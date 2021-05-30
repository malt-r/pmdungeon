package items.potions;

import items.IItemVisitor;

/**
 * Poison Potion.
 *
 * <p>Contains everything that describes a Poison potion.
 */
public class PoisonPotion extends Potion {
  protected int damageValue = 100;

  /**
   * Constructor of the Poison potion class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public PoisonPotion() {
    super();
    String[] idleLeftFrames = new String[] {"tileset/items/flask_big_green.png"};
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Returns the damage value of the potion which can be used for display purposes.
   *
   * @return damageBalue of the potion
   */
  public int getDamageValue() {
    return damageValue;
  }

  /**
   * Returns the name of the potion which can be used for display purposes.
   *
   * @return Name of the potion.
   */
  @Override
  public String getName() {
    return "Poison Potion";
  }

  /**
   * Returns the deescription of the potion which can be used for display purposes.
   *
   * @return description of the potion.
   */
  @Override
  protected String getDescription() {
    return "I'm a poisonly potion";
  }

  /**
   * Accept method for a item visitor to extend the functionality of the Potion class.
   *
   * @param visitor Visitor that visits the class
   */
  @Override
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }
}
