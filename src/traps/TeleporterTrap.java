package traps;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import util.math.Vec;

import static util.math.Convenience.checkForIntersection;

/**
 * The base class for any TeleporterTrap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public class TeleporterTrap extends Trap{
  private float activationDistance = 0.5f;
  private Point collisionCenterOffset = new Point(-0.3f, -0.3f);

  /**
   * Constructor of the TeleporterTrap class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public TeleporterTrap(){
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/traps/teleporter/teleporter.png",
            "tileset/traps/teleporter/teleporter_2.png",
            "tileset/traps/teleporter/teleporter_3.png",
            "tileset/traps/teleporter/teleporter_4.png",
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * {@inheritDoc}
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    this.draw(-1,-1);
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());

    // TODO: calc on setPosition
    Point collisionCenter= new Vec(this.getPosition()).add(new Vec(collisionCenterOffset)).toPoint();
    for (var entitiy : nearEntities) {
      if (!(entitiy instanceof Actor)) {continue;}
        var actor = (Actor) entitiy;
      if(!checkForIntersection(collisionCenter, entitiy.getPosition(), activationDistance)){continue;}
          actor.findRandomPosition();
    }
  }
}