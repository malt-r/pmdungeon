package traps;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;
import main.Game;
import util.math.Vec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static util.math.Convenience.checkForIntersection;

/**
 * The base class for any trap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public abstract class Trap extends DrawableEntity {

  protected float activationDistance = 1.f;
  protected Point collisionCenterOffset = new Point(0.f, 0.f);
  protected Point collisionCenterPoint = new Point(0.f, 0.f);

  /**
   * Constructor of the Trap base class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Trap() {

  }

  @Override
  protected void setPosition(Point position) {
    super.setPosition(position);
    this.collisionCenterPoint = new Vec(this.getPosition()).add(new Vec(collisionCenterOffset)).toPoint();
  }

  protected boolean checkForIntersectionWithDrawable(IDrawable drawable) {
    return checkForIntersection(collisionCenterPoint, drawable.getPosition(), activationDistance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/default/default_anim.png"
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }
}