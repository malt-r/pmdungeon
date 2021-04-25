package monsters;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.Game;
import main.ICombatable;

import java.util.*;

/**
 * The base class for any monster.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public abstract class Monster extends Actor {
  private final Timer respawnTimer;
  private final long respawnDelay;
  //Saves the direction of the last movement
  private Integer directionState=0;
  //Wether the direction of the moving monster should be updated
  private boolean updateDirectionState;
  private final Timer updateDirectionStateTimer;

  /**
   * Constructor of the Monster class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   *  @param game Game of the monster
   */
  public Monster(Game game) {
    super(game);
    updateDirectionStateTimer = new Timer();
    updateDirectionState=true;

    this.respawnTimer = new Timer();
    this.respawnDelay = 500;

    // combat-characteristics:
    health = 100.f;
    maxHealth = 100.f;

    baseHitChance = 0.6f;
    hitChanceModifier = 1.f;

    baseAttackDamage = 50;
    attackDamageModifier = 1.f;

    baseEvasionChance = 0.15f;
    evasionChanceModifier = 1.f;
  }
  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    super.update();
  }

  @Override
  public boolean isOtherFriendly(ICombatable other) {
    if (other instanceof Monster) {
      return true;
    }
    return false;
  }

  /**
   * Generates random Movement Inputs for natural moving of the monster
   */
  @Override
  protected Point readMovementInput(){
    if(hasTarget()){return new Point(this.position.x,this.position.y);}

    var newPosition = new Point(this.position.x,this.position.y);
    if(updateDirectionState){
      updateDirectionState=false;
      TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
          updateDirectionState = true;
        }
      };
      //TODO:describe matrix values in interval and in rows
      updateDirectionStateTimer.schedule(timerTask,200);
      var directionMatrix = new int[][]{
              { 40, 10, 10, 10, 15},
              { 10, 40, 10, 10, 15},
              { 10, 10, 40, 10, 15},
              { 10, 10, 10, 40, 15},
              { 30, 30, 30, 30, 40}
      };
      var max=100;
      var min = 1;
      int number = (int)  ((Math.random() * (max - min)) + min);
      int current=0;
      for (int i=0;i<directionMatrix[i].length;i++){
        current+= directionMatrix[i][directionState];
        if(number<current){
          directionState=i;
          break;
        }
      }
    }

    if (directionState ==0) {
      newPosition.y += movementSpeed;
    }
    if (directionState ==1) {
      newPosition.y -= movementSpeed;
    }
    if (directionState ==2) {
      newPosition.x += movementSpeed;
    }
    if (directionState ==3) {
      newPosition.x -= movementSpeed;
    }
  return newPosition;
  }
  /**
   * Manages damage given by other actors. Also starts a timer for respawning, when the monster has been slain.
   *
   * @param damage  The damage value that should be deducted from health.
   */
  @Override
  public void dealDamage(float damage, ICombatable attacker) {
    super.dealDamage(damage, attacker);
    if (this.isDead()) {
      TimerTask respawnTask = new TimerTask() {
        @Override
        public void run() {
          findRandomPosition();
          setHealth(maxHealth);
        }
      };
      respawnTimer.schedule(respawnTask, respawnDelay);
    }
  }
}