package monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Actor;
import main.Game;
import main.ICombatable;
import main.sample.MockMonster;

import java.util.*;
import java.util.logging.Logger;


public abstract class Monster extends Actor {
  static Logger l = Logger.getLogger(Monster.class.getName());
  private final Timer respawnTimer;
  private final long respawnDelay;



  private Integer directionState=0;

  // currently only two looking directions are supported (left and right),
  // therefore a boolean is sufficient to represent the
  // looking direction
  private boolean lookLeft;
  private boolean updateDirectionState;
  private Timer updateDirectionStateTimer;
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
  }
  @Override
  protected void generateAnimations(){
    String[] idleLeftFrames = new String[] {
            "tileset/lizard/lizard_m_idle_left_anim_f0.png",
            "tileset/lizard/lizard_m_idle_left_anim_f1.png",
            "tileset/lizard/lizard_m_idle_left_anim_f2.png",
            "tileset/lizard/lizard_m_idle_left_anim_f3.png"
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[] {
            "tileset/lizard/lizard_m_idle_anim_f0.png",
            "tileset/lizard/lizard_m_idle_anim_f1.png",
            "tileset/lizard/lizard_m_idle_anim_f2.png",
            "tileset/lizard/lizard_m_idle_anim_f3.png"
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[] {
            "tileset/lizard/lizard_m_run_left_anim_f0.png",
            "tileset/lizard/lizard_m_run_left_anim_f1.png",
            "tileset/lizard/lizard_m_run_left_anim_f2.png",
            "tileset/lizard/lizard_m_run_left_anim_f3.png"
    };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames = new String[] {
            "tileset/lizard/lizard_m_run_anim_f0.png",
            "tileset/lizard/lizard_m_run_anim_f1.png",
            "tileset/lizard/lizard_m_run_anim_f2.png",
            "tileset/lizard/lizard_m_run_anim_f3.png"
    };
    runAnimationRight = createAnimation(runRightFrames, 4);
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

  private void setCurrentAnimation(AnimationState animationState) {
    switch (animationState) {
      case RUN:
        this.currentAnimation =  lookLeft ? this.runAnimationLeft : this.runAnimationRight;
        break;
      case IDLE:
      default:
        this.currentAnimation =  lookLeft ? this.idleAnimationLeft : this.idleAnimationRight;
    }
  }

  /**
   * Get the current position in the DungeonWorld.
   * @return the current position in the DungeonWorld.
   */
  @Override
  public Point getPosition() {
    return position;
  }

  /**
   * Normalize the difference-vector between two Points on a defined basis.
   * @param p1 The origin of the vector
   * @param p2 The tip of the vector
   * @param normalizationBasis The basis on which the length of the difference-vector should be normalized.
   * @return A Point, of which the x and y members represent the components of the normalized vector.
   */
  private Point normalizeDelta(Point p1, Point p2, float normalizationBasis) {
    float diffX = p2.x - p1.x;
    float diffY = p2.y - p1.y;
    float magnitude = (float)Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

    if (magnitude != 0.0f) {
      var diffXNorm = diffX / magnitude * normalizationBasis;
      var diffYNorm = diffY / magnitude * normalizationBasis;
      return new Point(diffXNorm, diffYNorm);
    } else {
      return new Point(diffX, diffY);
    }
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
   * Override IEntity.deletable and return false for the hero.
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
    this.resetCombatStats();
    this.level = level;
    findRandomPosition();
  }





  protected void resetCombatStats() {
    super.resetCombatStats();
    l.info("Monster: resetting combat stats");
  }



  /**
   * Sets the current position of the Hero to a random position inside the DungeonWorld.
   */
  public void findRandomPosition() {
    l.info("I have spawned");
    this.position = new Point(level.getRandomPointInDungeon());
  }

  // ICombatible
  ICombatable target;
  //TODO: Verhalten zur Basisklasse klären
  float health = 100.f;
  float maxHealth = 100.f;
  float hitChance = 1.0f;
  float evasionChance = 0.0f;
  float damage = 10.f;



  @Override
  public void dealDamage(float damage) {
    super.dealDamage(damage);

    if (this.isDead()) {
      l.info("I am dead");

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