package items.scrolls;

import items.IItemVisitor;
/**
 * Speed scroll.
 * <p>
 *   Contains everything that describes a Speed Scroll.
 * </p>
 */
public class SpeedScroll extends Scroll {
  protected float speedMultiplier = 1.5f;
  /**
   *  Returns the speedMultiplier value of the potion which can be used for display purposes
   *  @return attackbonus of the scroll
   */
  public float getSpeedMultiplier(){
    return speedMultiplier;
  }
  /**
   *  Returns the name of the scroll which can be used for display purposes
   *  @return Name of the scroll
   */
  @Override
  public String getName() {
    return "Speedscroll";
  }
  /**
   *  Returns the deescription of the potion which can be used for display purposes
   *  @return description of the potion
   */
  @Override
  protected String getDescription() {
    return "A scroll that heals";
  }
  /**
   * Constructor of the Speedscroll class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public SpeedScroll(){
    super();
    String[] idleLeftFrames = new String[]{
            "tileset/items/speedscroll.png"

    };
    currentAnimation = createAnimation(idleLeftFrames, 9001);
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
