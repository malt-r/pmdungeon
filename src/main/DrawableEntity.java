package main;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * An base class entity which can be drawn.
 *
 * <p>Contains all animations, the current position in the DungeonWorld and movement logic.
 */
public abstract class DrawableEntity implements IAnimatable, IEntity {
  public static final Point LAST_POSITION_NOT_SET = new Point(-1, -1);
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // cache a reference to the game to be able to scan all entities for possible attack targets
  public static Game game;
  protected DungeonWorld level;
  protected Animation currentAnimation;
  protected ArrayList<DrawableEntityObserver> observers = new ArrayList<>();
  protected ArrayList<DrawableEntityObserver> observerToRemove = new ArrayList<>();
  private Point position;
  private Point lastPosition;

  /**
   * Constructor of the DrawableEntity class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public DrawableEntity() {
    game = Game.getInstance();
    generateAnimations();
    this.lastPosition = LAST_POSITION_NOT_SET;
    this.position = new Point(0, 0);
  }

  /** Generates the animations of the actor. */
  protected void generateAnimations() {
    String[] defaultFrame =
        new String[] {
          "tileset/default/default_anim.png",
        };
    currentAnimation = createAnimation(defaultFrame, Integer.MAX_VALUE);
  }

  /**
   * Creates an animation from textures.
   *
   * @param texturePaths array of textures that should be added to the animation
   * @param frameTime time between two textures
   * @return Animation containing the textures from texturePat
   */
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(
          new Texture(
              Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame))
                  .getPath()));
    }
    return new Animation(textureList, frameTime);
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
   * Get the current position in the DungeonWorld.
   *
   * @return the current position in the DungeonWorld.
   */
  @Override
  public Point getPosition() {
    return position;
  }

  protected void setPosition(Point position) {
    if (this.lastPosition.equals(LAST_POSITION_NOT_SET)) {
      this.lastPosition = this.position;
    }
    this.position = position;
    notifyDrawableEntityObservers();
  }

  public Point getLastPosition() {
    return this.lastPosition;
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    this.draw();
    this.removeObservers();
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

  /** Set reference to DungeonWorld and spawn player at random position in the level. */
  public void setLevel(DungeonWorld level) {
    this.level = level;
    findRandomPosition();
  }

  /** Sets the current position of the Hero to a random position inside the DungeonWorld. */
  public void findRandomPosition() {
    setPosition(new Point(level.getRandomPointInDungeon()));
  }

  /** Will be called to notify observers about state change. */
  private void notifyDrawableEntityObservers() {
    for (var observer : observers) {
      observer.update(this);
    }
    this.lastPosition = LAST_POSITION_NOT_SET;
  }

  /**
   * Register observer to add to notification list.
   *
   * @param observer The observer to register.
   */
  public void register(DrawableEntityObserver observer) {
    if (!this.observers.contains(observer)) {
      this.observers.add(observer);
    }
  }

  /**
   * Schedule observer for removal from notification list. Will be removed in next update.
   *
   * @param observer The observer to remove from notification list.
   */
  public void unregister(DrawableEntityObserver observer) {
    if (this.observers.contains(observer) && !this.observerToRemove.contains(observer)) {
      this.observerToRemove.add(observer);
    }
  }

  /**
   * Effectively remove the observers from notification list, which were scheduled for removal since
   * last call.
   */
  protected void removeObservers() {
    for (var observer : observerToRemove) {
      this.observers.remove(observer);
    }
    observerToRemove.clear();
  }
}
