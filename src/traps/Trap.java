package traps;

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
import java.util.logging.Logger;
/**
 * The base class for any trap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public abstract class Trap implements IAnimatable, IEntity {
  protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  protected Animation currentAnimation;
  protected Game game;
  protected Point position;
  protected DungeonWorld level;
  /**
   * Constructor of the Trap base class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Trap(){
    this.game = Game.getInstance();
    String[] idleLeftFrames = new String[]{
            "tileset/default/default_anim.png",
    };
    currentAnimation = createAnimation(idleLeftFrames, 6);
  }

  /**
   *
   * @param texturePaths array of textures that should be added to the animation
   * @param frameTime time between two textures
   * @return returns an Animation containing the specifed textures
   */
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
    }
    return new Animation(textureList, frameTime);
  }
  /**
   * Get the current position in the DungeonWorld.
   *
   * @return the current position in the DungeonWorld.
   */
  @Override
  public Point getPosition() {
    return position;
  }

  /**
   * Returns the active animation which can be used to draw the trap
   * @return current Animation of the trap
   */
  @Override
  public Animation getActiveAnimation() {
    return currentAnimation;
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
      this.draw();
  }
  /**
   * Override IEntity.deletable and return false for the actor.
   *
   * @return false
   */
  @Override
  public boolean deleteable() {
    return false;
  }

  /**
   * Set reference to DungeonWorld and spawn player at random position in the level.
   */
  public void setLevel(DungeonWorld level) {
    this.level = level;
    findRandomPosition();
  }
  /**
   * Sets the current position of the Hero to a random position inside the DungeonWorld.
   */
  public void findRandomPosition() {
    this.position = new Point(level.getRandomPointInDungeon());
  }
}
