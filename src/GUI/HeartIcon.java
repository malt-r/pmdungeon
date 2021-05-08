package GUI;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IHUDElement;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class HeartIcon implements IHUDElement {

    private Point position;
    private Texture texture;
    private Texture defaultTexture = new Texture("tileset/default/default_anim_seethrough.png");
    private Texture heartFull = new Texture("tileset/other/ui_heart_full.png");
    private Texture heartHalf = new Texture("tileset/other/ui_heart_half.png");
    private Texture heartEmpty = new Texture("tileset/other/ui_heart_empty.png");

    public HeartIcon(int index){
        position = new Point((float)index * 0.5f, 4.5f);
        texture = heartFull;
    }

    public void setState(int state){
        switch (state){
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
}
