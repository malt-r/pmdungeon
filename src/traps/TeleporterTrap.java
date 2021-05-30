package traps;

import main.Actor;

/**
 * The base class for any TeleporterTrap.
 *
 * <p>Contains all animations, the current position in the DungeonWorld.
 */
public class TeleporterTrap extends Trap {

  /**
   * Constructor of the TeleporterTrap class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public TeleporterTrap() {
    super();
    super.collisionCenterOffset.x = -0.3f;
    super.collisionCenterOffset.y = -0.3f;
    super.activationDistance = 0.5f;
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/traps/teleporter/teleporter.png",
          "tileset/traps/teleporter/teleporter_2.png",
          "tileset/traps/teleporter/teleporter_3.png",
          "tileset/traps/teleporter/teleporter_4.png",
        };
    currentAnimation = createAnimation(idleLeftFrames, 6);
  }

  /**
   * {@inheritDoc} Called each frame, handles movement and the switching to and back from the
   * running animation state.
   */
  @Override
  public void update() {
    this.draw(-1, -1);
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
    for (var entitiy : nearEntities) {
      if (entitiy instanceof Actor) {
        var actor = (Actor) entitiy;
        if (super.checkForIntersectionWithDrawable(entitiy)) {
          actor.findRandomPosition();
        }
      }
    }
  }
}
