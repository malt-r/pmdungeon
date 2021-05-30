package traps;

import items.Item;
import monsters.MonsterType;

import java.util.Random;

import static util.math.Convenience.checkForIntersection;

/**
 * The base class for a Activator trap.
 *
 * <p>Contains all animations, the current position in the DungeonWorld.
 */
public class ActivatorTrap extends Trap {
  protected boolean isActivated;

  /**
   * Constructor of the Activator Trap class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public ActivatorTrap() {
    super();
    isActivated = false;
  }

  /**
   * The Trap can only be activated once.
   *
   * @return if the trap has been activated
   */
  public boolean isActivated() {
    return isActivated;
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/traps/trap-activator/column_top.png",
        };
    currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    this.draw(-1f, -0.75f);
    checkIfActivated();
  }

  // Check if item lays on it
  // to spawn a monster
  private boolean checkIfActivated() {
    var nearEntities = game.getEntitiesAtPoint(this.getPosition());
    for (var entitiy : nearEntities) {
      if ((entitiy instanceof Item)) {
        var item = (Item) entitiy;
        if (checkForIntersection(this, item)) {
          if (!isActivated) {
            isActivated = true;
            mainLogger.info("Trap activated");
            spawnMonsters();
            return true;
          }
        }
      }
    }
    return false;
  }

  private void spawnMonsters() {
    try {
      Random rand = new Random();
      int upperbound = 100;

      for (int i = 0; i < 3; i++) {
        int int_random = rand.nextInt(upperbound);
        if (int_random % 2 == 0) {
          game.spawnMonster(MonsterType.LIZARD, this.getPosition());
        } else {
          game.spawnMonster(MonsterType.DEMON, this.getPosition());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
