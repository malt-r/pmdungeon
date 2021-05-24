package traps;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import main.Actor;

/**
 * The base class for any TeleporterTrap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public class TeleporterTrap extends Trap{

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
    var allEntities = game.getAllEntities();
    for(var entitiy : allEntities){
      if (!(entitiy instanceof Actor)) {continue;}
        var actor = (Actor) entitiy;
        if(!game.checkForIntersection(this, (IDrawable) entitiy,level)){continue;}
          actor.findRandomPosition();
    }
  }
}