package monsters.strategies.movement;

import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.Timer;
import java.util.TimerTask;

/** Movement Strategy for random movement. */
public class RandomMovementStrategy implements MovementStrategy {

  private final Timer updateDirectionStateTimer;
  // Wether the direction of the moving monster should be updated
  protected boolean updateDirectionState;
  // Movementspeed
  protected float movementSpeed = 0.1f;
  // Saves the direction of the last movement
  private Integer directionState = 0;

  /** Constructor of the Random Movementstrategy. */
  public RandomMovementStrategy() {
    updateDirectionStateTimer = new Timer();
    updateDirectionState = true;
  }

  /** {@inheritDoc} */
  @Override
  public Point Move(Point currentPosition, DungeonWorld level) {
    var newPosition = new Point(currentPosition);
    if (updateDirectionState) {
      updateDirectionState = false;
      TimerTask timerTask =
          new TimerTask() {
            @Override
            public void run() {
              updateDirectionState = true;
            }
          };
      updateDirectionStateTimer.schedule(timerTask, 200);
      var directionMatrix =
          new int[][] {
            {40, 10, 10, 10, 15},
            {10, 40, 10, 10, 15},
            {10, 10, 40, 10, 15},
            {10, 10, 10, 40, 15},
            {30, 30, 30, 30, 40}
          };
      var max = 100;
      var min = 1;
      int number = (int) ((Math.random() * (max - min)) + min);
      int current = 0;
      for (int i = 0; i < directionMatrix[i].length; i++) {
        current += directionMatrix[i][directionState];
        if (number < current) {
          directionState = i;
          break;
        }
      }
    }

    if (directionState == 0) {
      newPosition.y += movementSpeed;
    }
    if (directionState == 1) {
      newPosition.y -= movementSpeed;
    }
    if (directionState == 2) {
      newPosition.x += movementSpeed;
    }
    if (directionState == 3) {
      newPosition.x -= movementSpeed;
    }
    return newPosition;
  }
}
