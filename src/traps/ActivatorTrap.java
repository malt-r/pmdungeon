package traps;

import items.Item;
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
    this.draw(-1f,0.25f);
    checkIfActivated();

  }

  //Check if item lays on it
  //to spawn a monster
  private boolean checkIfActivated(){
    var allEntities = game.getAllEntities();
    for(var entitiy : allEntities){
      if (!(entitiy instanceof Item)) { continue; }
      var item = (Item) entitiy;
      if (!game.checkForIntersection(this, item, level)){ return false;  }
      if (isActivated){ return false; }
      isActivated = true;
      System.out.println("I was activated mothafucka");
      spawnMonsters();
      return true;
    }
    return false;
  }

  private void spawnMonsters(){
    try {
      game.spawnMonster(MonsterType.DEMON,this.position);
      game.spawnMonster(MonsterType.DEMON,this.position);
      game.spawnMonster(MonsterType.DEMON,this.position);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
