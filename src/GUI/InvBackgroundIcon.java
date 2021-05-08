package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class InvBackgroundIcon implements IHUDElement {

    private Point position;
    private Texture texture;
    private Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");
    private Texture pointer = new Texture("tileset/default/floor_1_test.png");
    private Texture defaultBackground = new Texture("textures/dungeon/floor/floor_1.png");

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

    public void setDefaultTexture(){
        texture = defaultTexture;
    }

    public void setPointerTexture(){ texture = pointer; }

    public void setDefaultBackgroundTexture(){ texture = defaultBackground; }
}
