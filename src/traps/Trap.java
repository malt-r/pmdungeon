package traps;

import static util.math.Convenience.checkForIntersection;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;
import util.math.Vec;


/**
 * The base class for any trap.
 *
 * <p>Contains all animations, the current position in the DungeonWorld.
 */
public abstract class Trap extends DrawableEntity {

  protected float activationDistance = 1.f;
  protected Point collisionCenterOffset = new Point(0.f, 0.f);
  protected Point collisionCenterPoint = new Point(0.f, 0.f);

  /**
   * Constructor of the Trap base class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Trap() {}

  @Override
  protected void setPosition(Point position) {
    super.setPosition(position);
    this.collisionCenterPoint =
        new Vec(this.getPosition()).add(new Vec(collisionCenterOffset)).toPoint();
  }

  protected boolean checkForIntersectionWithDrawable(IDrawable drawable) {
    return checkForIntersection(collisionCenterPoint, drawable.getPosition(), activationDistance);
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[] {"tileset/default/default_anim.png"};
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
}
