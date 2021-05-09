package traps;

import items.Item;
import main.Hero;
import monsters.MonsterType;

import java.util.Random;

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
    this.draw(-1f,-0.75f);
    checkIfActivated();

  }

  //Check if item lays on it
  //to spawn a monster
  private boolean checkIfActivated(){
    var allEntities = game.getAllEntities();
    for(var entitiy : allEntities){
      if (!(entitiy instanceof Item)) { continue; }
      var item = (Item) entitiy;
      if (!game.checkForIntersection(this, item, level)){ continue;  }
      if (isActivated){ return false; }
      isActivated = true;
      mainLogger.info("Trap activated");
      spawnMonsters();
      return true;
    }
    return false;
  }

  private void spawnMonsters(){
    try {
      Random rand = new Random();
      int upperbound = 100;

      for(int i =0;i<3;i++){
        int int_random = rand.nextInt(upperbound);
      if(int_random %2==0){
        game.spawnMonster(MonsterType.LIZARD,this.position);
      }
      else{
        game.spawnMonster(MonsterType.DEMON,this.position);
      }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
