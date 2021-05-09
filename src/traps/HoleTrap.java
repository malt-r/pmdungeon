package traps;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import main.ICombatable;
/**
 * The base class for any HoleTrap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public class HoleTrap extends Trap{
  /**
   * Constructor of the HoleTrap class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public HoleTrap(){
    super();
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
    var allEntities = game.getAllEntities();
    for(var entitiy : allEntities){
      if (entitiy instanceof IDrawable && entitiy instanceof ICombatable) {
      if(game.checkForIntersection(this,(IDrawable) entitiy,level)){
        ICombatable victim = (ICombatable) entitiy;
        victim.dealDamage(Float.MAX_VALUE,null);
      }
      }
    }

  }
}
