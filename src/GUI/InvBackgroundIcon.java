package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The InvBackgroundIcon for the HUD
 *
 * <p>Contains all textures, positions and texture manipulation logic
 */
public class InvBackgroundIcon implements IHUDElement {

  private final Point position;
  private final Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");
  private final Texture pointer = new Texture("tileset/default/inv_pointer.png");
  private final Texture defaultBackground = new Texture("textures/dungeon/floor/floor_1.png");
  private Texture texture;

  /**
   * Constructor of InvBackgroundIcon class.
   *
   * <p>This constructor will instantiate a new InvBackgroundIcon, sets ist default texture and
   * position
   *
   * @param index of inventory slot
   * @param height of position
   */
  public InvBackgroundIcon(int index, float height) {
    position = new Point((float) index * 0.5f, height);
    texture = defaultTexture;
  }

  /**
   * Returns the position of the icon for display purposes
   *
   * @return position of the icon
   */
  @Override
  public Point getPosition() {
    return position;
  }

  /**
   * Returns the texture of the icon for display purposes
   *
   * @return texture of the icon
   */
  @Override
  public Texture getTexture() {
    return texture;
  }

  /** Sets the default texture (seethrough) */
  public void setDefaultTexture() {
    texture = defaultTexture;
  }

  /** Sets the index/pointer texture */
  public void setPointerTexture() {
    texture = pointer;
  }

  /** Sets the default background texture */
  public void setDefaultBackgroundTexture() {
    texture = defaultBackground;
  }
}
