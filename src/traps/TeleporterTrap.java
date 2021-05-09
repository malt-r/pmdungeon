package traps;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import main.Actor;


public class TeleporterTrap extends Trap{
  public TeleporterTrap(){
    super();
    String[] idleLeftFrames = new String[]{
            "tileset/traps/teleporter/teleporter.png",
            "tileset/traps/teleporter/teleporter_2.png",
            "tileset/traps/teleporter/teleporter_3.png",
            "tileset/traps/teleporter/teleporter_4.png",

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
      if (!(entitiy instanceof Actor)) {continue;}
        var actor = (Actor) entitiy;
        if(!game.checkForIntersection(this, (IDrawable) entitiy,level)){continue;}
          actor.findRandomPosition();
    }

  }
}
