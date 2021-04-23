package monsters;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.Game;
import main.ICombatable;
import java.util.*;


public abstract class Monster extends Actor {
  private final Timer respawnTimer;
  private final long respawnDelay;
  private Integer directionState=0;

  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;
  private boolean updateDirectionState;
  private final Timer updateDirectionStateTimer;
  private enum AnimationState {
    IDLE,
    RUN,
  }

  /**
   * Constructor of the Hero class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
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

  protected Animation createAnimation(String[] texturePaths, int frameTime)
  {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
    }
    return new Animation(textureList, frameTime);
  }
  /**
   * Determine the active animation which should be played.
   * @return The active animation.
   */
  @Override
  public Animation getActiveAnimation() {
    return this.currentAnimation;
  }
  /**
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    super.update();
  }
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
  @Override
  public void dealDamage(float damage) {
    super.dealDamage(damage);
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