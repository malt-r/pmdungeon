package traps;

import main.ICombatable;

/**
 * The base class for any HoleTrap.
 *
 * <p>Contains all animations, the current position in the DungeonWorld.
 */
public class HoleTrap extends Trap {

  /**
   * Constructor of the HoleTrap class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public HoleTrap() {
    super();
    super.activationDistance = 0.6f;
    super.collisionCenterOffset.x = -0.2f;
    super.collisionCenterOffset.y = -0.3f;
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/traps/hole/hole.png",
        };
    currentAnimation = createAnimation(idleLeftFrames, 6);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    this.draw(-1, -1);
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
    for (var entitiy : nearEntities) {
      if (entitiy instanceof ICombatable) {
        if (super.checkForIntersectionWithDrawable(entitiy)) {
          ICombatable victim = (ICombatable) entitiy;
          victim.dealDamage(Float.MAX_VALUE, null);
        }
      }
    }
  }
}
