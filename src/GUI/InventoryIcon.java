package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class InventoryIcon implements IHUDElement {

    private Point position;
    private Texture texture;

    public InventoryIcon (int index){
        position = new Point((float)index * 0.5f, 0.0f);
        texture = new Texture("tileset/default/default_anim.png");
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
        //this.texture.dispose();
        this.texture = texture;
    }
}
