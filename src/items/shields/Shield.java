package items.shields;

import items.EquipableItem;

/**
 * Shield.
 *
 * <p>Contains everything that describes a shield.
 */
public abstract class Shield extends EquipableItem {
  // protected float defenseValue;

  /**
   * Constructor of the Shield class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Shield() {
    super();
  }

  /** Called each frame and draws the shield. */
  @Override
  public void update() {
    draw(-0.85F, -0.85F, 0.75f, 0.75f);
  }
}
