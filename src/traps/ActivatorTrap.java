package traps;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.Item;
import main.ICombatable;
import main.Spawner;
import monsters.MonsterType;

public class ActivatorTrap extends Trap{
  protected  boolean isActivated;

  public boolean isActivated() {
    return isActivated;
  }

  public ActivatorTrap(){
    super();
    isActivated=false;
    String[] idleLeftFrames = new String[]{
            "tileset/traps/trap-activator/column_top.png",
    };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    //this.draw();
    this.draw(0,0.25f);
    //Check if item lays on it
    //to spawn a monster
    var allEntities = game.getAllEntities();
    for(var entitiy : allEntities){
      if (entitiy instanceof IDrawable && entitiy instanceof Item) {
        if(game.checkForIntersection(this,(IDrawable) entitiy,level)){
          if(!isActivated){
            isActivated = true;
            System.out.println("I was activated mothafucka");
            try {
              game.spawnMonster(MonsterType.DEMON,this.position);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }


 // @Override
  public void update2() {
    this.draw(0,0.25f);
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
