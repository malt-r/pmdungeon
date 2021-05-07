package traps;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.ICombatable;

import javax.swing.text.Position;
import java.util.ArrayList;

public class HoleTrap extends Trap{
  public HoleTrap(){
    super();
    String[] idleLeftFrames = new String[]{
            "tileset/traps/hole/hole.png",
    };
    currentAnimation = createAnimation(idleLeftFrames, 6);
  }
  @Override
  public Animation getActiveAnimation() {
    return currentAnimation;
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
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    //this.draw();
    this.draw(-1,-1);
    if(game.checkForTrigger(this.position)){
      ICombatable hero = (ICombatable) game.getAllEntities().get(0);
      hero.dealDamage(Float.MAX_VALUE,null);
    }
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
