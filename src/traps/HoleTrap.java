package traps;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.ICombatable;
import util.math.Vec;

import static util.math.Convenience.checkForIntersection;

/**
 * The base class for any HoleTrap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public class HoleTrap extends Trap{

  private float activationDistance = 0.6f;
  private Point collisionCenterOffset = new Point(-0.2f, -0.3f);
  /**
   * Constructor of the HoleTrap class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public HoleTrap(){
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateAnimations(){
    String[] idleLeftFrames = new String[]{
            "tileset/traps/hole/hole.png",
    };
    currentAnimation = createAnimation(idleLeftFrames, 6);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    this.draw(-1,-1);
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
    Point collisionCenter= new Vec(this.getPosition()).add(new Vec(collisionCenterOffset)).toPoint();
    for(var entitiy : nearEntities){
      if (entitiy instanceof IDrawable && entitiy instanceof ICombatable) {
        if(checkForIntersection(collisionCenter, entitiy.getPosition(), activationDistance)) {
          ICombatable victim = (ICombatable) entitiy;
          victim.dealDamage(Float.MAX_VALUE,null);
        }
      }
    }
  }
}
