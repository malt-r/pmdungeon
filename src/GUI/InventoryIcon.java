package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The InventoryIcon for the HUD
 * <p>
 *     Contains all textures, positions and texture manipulation logic
 * </p>
 */
public class InventoryIcon implements IHUDElement {

    private Point position;
    private Texture texture;
    private Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");

    /**
     * Constructor of InventoryIcon class.
     * <p>
     *     This constructor will instantiate a new InventoryIcon, sets ist default texture and position
     * </p>
     * @param index of inventory slot
     * @param height of position
     *
     */
    public InventoryIcon (int index, float height){
        position = new Point((float)index * 0.5f, height);
        texture = defaultTexture;
    }
    /**
     * Returns the texture of the icon for display purposes
     * @return texture of the icon
     */
    @Override
    public Point getPosition() {
        return position;
    }
    /**
     * Returns the texture of the icon for display purposes
     * @return texture of the icon
     */
    @Override
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets the texture of the icon for display pruposes
     * @param texture which should be used for the icon
     */
    public void setTexture(Texture texture){
        this.texture = texture;
    }

    /**
     * Sets the default texture (seethrough)
     */
    public void setDefaultTexture(){
        texture = defaultTexture;
    }

}
