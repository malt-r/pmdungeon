package mock;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import main.Hero;

import java.util.ArrayList;
import java.util.List;

public class MockHero extends Hero {

    public MockHero() {

    }

    @Override
    protected Animation createAnimation(String[] texturePaths, int frameTime) {
        return null;
    }
}
