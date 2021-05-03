package items.potions;

import items.IItemVisitor;
import main.Game;

/**
 * Health Potion.
 * <p>
 *   Contains everything that describes a Health potion.
 * </p>
 */
public class HealthPotion extends Potion {
  protected int healValue = 100;
  /**
   *  Returns the heal value of the potion which can be used for display purposes
   *  @return healvalue of the potion
   */
  public int getHealValue(){ return healValue; }
  /**
   *  Returns the name of the potion which can be used for display purposes
   *  @return Name of the potion
   */
  @Override
  public String getName() {
    return "Health Potion";
  }

  /**
   *  Returns the deescription of the potion which can be used for display purposes
   *  @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "A potion that heals";
  }

  /**
   * Constructor of the Healthpotion class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public HealthPotion(Game game){
    super(game);
    String[] idleLeftFrames = new String[]{
            "tileset/items/flask_big_red.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, 9001);
  }

  /**
   * Called each frame and draws the potion.
   */
  @Override
  public void update(){
    drawWithScaling(1.0f,1.0f);
  }

  /**
   * Accept method for a item visitor to extend the functionality of the Potion class.
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }

}
