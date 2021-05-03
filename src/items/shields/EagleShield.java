package items.shields;

import items.IItemVisitor;
import main.Game;
/**
 * Eagle Shield.
 * <p>
 *   Contains everything that describes a Attacks croll.
 * </p>
 */
public class EagleShield extends Shield{
  /**
   * Constructor of the EagleShield class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public EagleShield(Game game) {
    super(game);
    this.defenseValue= 1.3f;
    String[] idleLeftFrames = new String[]{
            "tileset/items/shield_red_yellow_eagle.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
  /**
   *  Returns the name of the shield which can be used for display purposes
   *  @return Name of the scroll
   */
  @Override
  public String getName() {
    return "The mighty red Shield";
  }
  /**
   *  Returns the deescription of the shield which can be used for display purposes
   *  @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "Forgotten for centuries, one would say that only one of this kind of shield exists.";
  }
  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }

}
