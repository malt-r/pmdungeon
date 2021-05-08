package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class InventoryIcon implements IHUDElement {

    private Point position;
    private Texture texture;
    private Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");

    public InventoryIcon (int index, float hight){
        position = new Point((float)index * 0.5f, hight);
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

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setDefaultTexture(){
        texture = defaultTexture;
    }

}
