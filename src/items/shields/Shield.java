package items.shields;

import items.EquipableItem;
import main.Game;
/**
 * Shield.
 * <p>
 *   Contains everything that describes a shield.
 * </p>
 */
public abstract class Shield extends EquipableItem {
  protected float defenseValue;

  /**
   * Constructor of the Shield class.
   * <p>
   * This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public Shield(Game game) {
    super(game);
  }

  /**
   *  Returns the defense value of the shield which can be used for display purposes
   *  @return defensevalue of the shield
   */
  public float getDefenseValue(){
    return defenseValue;
  }
}
