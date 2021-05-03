package items.scrolls;

import items.IItemVisitor;
import main.Game;
/**
 * Attack Scroll.
 * <p>
 *   Contains everything that describes a Attacks croll.
 * </p>
 */
public class AttackScroll extends Scroll {
  protected int attackBonus = 5;
  /**
   *  Returns the heal value of the potion which can be used for display purposes
   *  @return attackbonus of the scroll
   */
  public int getAttackBonus(){
    return attackBonus;
  }
  /**
   *  Returns the name of the scroll which can be used for display purposes
   *  @return Name of the scroll
   */
  @Override
  public String getName() {
    return "Attackscroll";
  }
  /**
   *  Returns the deescription of the potion which can be used for display purposes
   *  @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "A scroll that increases attack value";
  }
  /**
   * Constructor of the Attackscroll class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public AttackScroll(Game game){
    super(game);
    String[] idleLeftFrames = new String[]{
            "tileset/items/attackscroll.png"

    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
  /**
   * Called each frame and draws the scroll.
   */
  @Override
  public void update(){
    drawWithScaling(0.75f,0.75f);
  }
  /**
   * Accept method for a item visitor to extend the functionality of the scroll class.
   */
  @Override
  public void accept(IItemVisitor visitor){
    visitor.visit(this);
  }
}
