package items.chests;

import items.IInventoryOpener;
import items.Item;
import items.ItemFactory;
import items.inventory.Inventory;
import main.DrawableEntity;
import main.Game;

/**
 * chest class
 *
 * <p>Defines a basic default chest.
 */
public class Chest extends DrawableEntity {
  Inventory inventory;

  /**
   * Constructor of the Chest class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Chest() {
    String[] idleFrame =
        new String[] {
          "tileset/other/chest_empty_open_anim_f0.png",
        };
    currentAnimation = createAnimation(idleFrame, 4);

    this.inventory = new Inventory(this, 10);
    generateContents();
    this.getInventory().register(Game.getInstance().getInventoryOverview());
  }

  /**
   * Returns the inventory of the chest.
   *
   * @return inventory of the chest
   */
  public Inventory getInventory() {
    return inventory;
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
   * Opens the inventory of the chest.
   *
   * @param opener opener of the chest
   */
  public void open(IInventoryOpener opener) {
    this.inventory.open(opener);
  }

  /**
   * Called each frame, handles movement and the switching to and back from the running animation
   * state.
   */
  @Override
  public void update() {
    this.inventory.update();
    this.draw(-1f, -0.75F, 1, 1);
  }
}
