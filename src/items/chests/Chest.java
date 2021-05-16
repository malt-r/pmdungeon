package items.chests;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import items.IInventoryOpener;
import items.Item;
import items.ItemFactory;
import items.ItemType;
import items.inventory.Inventory;
import main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * chest class
 * <p>
 *     Defines a basic default chest.
 * </p>
 */
public class Chest implements IAnimatable, IEntity {
    Animation idleAnimation;
    Point position;
    DungeonWorld level;

    Inventory inventory;
    /**
     * Determine the active animation which should be played.
     *
     * @return The active animation.
     */
    @Override
    public Animation getActiveAnimation() {
        return idleAnimation;
    }

    /**
     * Returns the inventory of the chest
     * @return inventory of the chest
     */
    public Inventory getInventory(){ return inventory; }
    /**
     * Constructor of the Chest class.
     * <p>
     * This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Chest() {
        String[] idleFrame = new String[]{
                "tileset/other/chest_empty_open_anim_f0.png",
        };
        idleAnimation = createAnimation(idleFrame, 4);

        this.inventory = new Inventory(this, 10);
        generateContents();
        this.getInventory().register(Game.getInstance());
    }

    private void generateContents() {
        int min = 2;
        int max = 5;

        int numItems = util.math.Convenience.getRandBetween(min, max);
        var items = ItemFactory.CreateRandomItems(numItems);
        for (Item item : items) {
            this.inventory.addItem(item);
        }
    }
    /**
     *
     * @param texturePaths array of textures that should be added to the animation
     * @param frameTime time between two textures
     * @return
     */
    protected Animation createAnimation(String[] texturePaths, int frameTime) {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {
            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }

    /**
     * Opens the inventory of the chest
     * @param opener opener of the chest
     */
    public void open(IInventoryOpener opener) {
        this.inventory.open(opener);
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
        this.inventory.update();
        this.draw(-1f,-0.75F,1,1);

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
