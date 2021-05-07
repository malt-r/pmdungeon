package items;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Abstract Base class for  items.
 * <p>
 *   Contains everything that describes an  item.
 * </p>
 */
public abstract class Item implements IAnimatable, IEntity{
  protected final Game game = Game.getInstance();
  protected DungeonWorld level;
  protected Animation currentAnimation;
  protected Point position;
  public boolean isStackable() {
    return true;
  }
  public abstract String getName();
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
   * Marks an entity as deletable so it can be removed from the game
   * @return if entity can be deleted
   */
  @Override
  public boolean deleteable(){
    return false;
  }
  /**
   * Determine the active animation which should be played.
   *
   * @return The active animation.
   */
  @Override
  public Animation getActiveAnimation() {
    return this.currentAnimation;
  }
  /**
   * Sets the current position of the Hero to a random position inside the DungeonWorld.
   */
  public void findRandomPosition() {
    this.position = new Point(level.getRandomPointInDungeon());
  }
  /**
   * Set reference to DungeonWorld and spawn player at random position in the level.
   */
  public void setLevel(DungeonWorld level){
    this.level = level;
  findRandomPosition();
  }

  /**
   *
   * @param texturePaths array of textures that should be added to the animation
   * @param frameTime time between two textures
   * @return
   */
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
    }
    return new Animation(textureList, frameTime);
  }

  /**
   * Accept method for accepting an ItemVisitpr
   * @param visitor visitor which should be accepted
   */
  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }

  /**
   *  Returns the Position of the item for drawing
   * @return position of the item
   */
  @Override
  public Point getPosition() {
    return position;
  }

  /**
   * Sets the position of the item when spawning or dropping
   * @param newPosition the position where the itme should be moved to
   */
  public void setPosition(Point newPosition){
    this.position = newPosition;
  }
}
