package items;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.inventory.Inventory;
import main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chest implements IAnimatable, IEntity {
    Animation idleAnimation;
    Point position;
    DungeonWorld level;
    Game game;

    Inventory inventory;

    @Override
    public Animation getActiveAnimation() {
        return idleAnimation;
    }

    public Chest(Game game) {
        String[] idleFrame = new String[]{
                "tileset/other/chest_empty_open_anim_f0.png",
        };
        idleAnimation = createAnimation(idleFrame, 4);

        this.inventory = new Inventory(this, 10, game);
        generateContents();
    }

    private void generateContents() {
        int min = 0;
        int max = 3;
        int num = (int)((Math.random() * (max - min)) + min);

        for (int i = 0; i < num; i++) {
            int iter = (int)((Math.random() * (ItemType.values().length)));

            for (int n = 0; n < ItemType.values().length; n++ ) {
                if (iter == n) {
                    try {
                        this.inventory.addItem(ItemFactory.CreateItem(ItemType.values()[n], this.game));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected Animation createAnimation(String[] texturePaths, int frameTime) {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {
            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }


    /**
     * Get the current position in the DungeonWorld.
     *
     * @return the current position in the DungeonWorld.
     */
    @Override
    public Point getPosition() {
        return position;
    }


    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        this.draw();
    }

    /**
     * Override IEntity.deletable and return false for the actor.
     *
     * @return false
     */
    @Override
    public boolean deleteable() {
        return false;
    }

    /**
     * Set reference to DungeonWorld and spawn player at random position in the level.
     */
    public void setLevel(DungeonWorld level) {
        this.level = level;
        findRandomPosition();
    }

    /**
     * Sets the current position of the Hero to a random position inside the DungeonWorld.
     */
    public void findRandomPosition() {
        this.position = new Point(level.getRandomPointInDungeon());
    }
}
