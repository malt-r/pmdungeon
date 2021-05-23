package items;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;

/**
 * Abstract Base class for  items.
 * <p>
 *   Contains everything that describes an  item.
 * </p>
 */
public abstract class Item extends DrawableEntity {

  /**
   * Returns the name of the item
   * @return name of the item
   */
  public abstract String getName();

  /**
   * Returns the description of an item
   * @return description of the item
   */
  protected abstract String getDescription();
  /**
   * Constructor of the item class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Item(){}
  /**
   * Called each frame and draws the regular sword with the right scaling.
   */
  @Override
  public void update(){
    this.draw();
  }

  /**
   * Accept method for accepting an ItemVisitpr
   * @param visitor visitor which should be accepted
   */
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }

  /**
   * Sets the position of the item when spawning or dropping
   * @param newPosition the position where the itme should be moved to
   */
  public void setPosition(Point newPosition){
    this.position = newPosition;
  }
}
