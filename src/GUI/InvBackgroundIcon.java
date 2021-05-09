package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * The InvBackgroundIcon for the HUD
 * <p>
 *     Contains all textures, positions and texture manipulation logic
 * </p>
 */
public class InvBackgroundIcon implements IHUDElement {

    private Point position;
    private Texture texture;
    private Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");
    private Texture pointer = new Texture("tileset/default/floor_1_test.png");
    private Texture defaultBackground = new Texture("textures/dungeon/floor/floor_1.png");

    /**
     * Constructor of InvBackgroundIcon class.
     * <p>
     *     This constructor will instantiate a new InvBackgroundIcon, sets ist default texture and position
     * </p>
     * @param index of inventory slot
     * @param height of position
     *
     */
    public InvBackgroundIcon (int index, float height){
        position = new Point((float)index * 0.5f, height);
        texture = defaultTexture;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets the default texture (seethrough)
     */
    public void setDefaultTexture(){
        texture = defaultTexture;
    }

    /**
     * Sets the index/pointer texture
     */
    public void setPointerTexture(){ texture = pointer; }

    /**
     * Sets the default background texture
     */
    public void setDefaultBackgroundTexture(){ texture = defaultBackground; }
}
