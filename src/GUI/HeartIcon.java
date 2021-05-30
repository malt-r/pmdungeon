package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The HeartIcon for the HUD
 *
 * <p>Contains all textures, positions and texture manipulation logic
 */
public class HeartIcon implements IHUDElement {

  private final Point position;
  private final Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");
  private final Texture heartFull = new Texture("tileset/other/ui_heart_full.png");
  private final Texture heartHalf = new Texture("tileset/other/ui_heart_half.png");
  private final Texture heartEmpty = new Texture("tileset/other/ui_heart_empty.png");
  private Texture texture;

  /**
   * Constructor of HeartIcon class.
   *
   * <p>This constructor will instantiate a new HeartIcon, sets ist default texture and position
   *
   * @param index of heart
   */
  public HeartIcon(int index) {
    position = new Point((float) index * 0.5f, 4.5f);
    texture = heartFull;
  }

  /**
   * Sets the state of the heart Icon
   *
   * @param state state which should be set
   */
  public void setState(int state) {
    switch (state) {
      case 0:
        texture = heartEmpty;
        break;
      case 1:
        texture = heartHalf;
        break;
      case 2:
        texture = heartFull;
        break;
      default:
        break;
    }
  }

  /**
   * returns the position of the hearth for display purposes
   *
   * @return position of the heartIcon
   */
  @Override
  public Point getPosition() {
    return position;
  }

  /**
   * gets the texture of the heartIcon for display purposes
   *
   * @return texture of the hearthicon
   */
  @Override
  public Texture getTexture() {
    return texture;
  }

  /** Sets the default texture (seethrough) */
  public void setDefaultTexture() {
    texture = defaultTexture;
  }
}
