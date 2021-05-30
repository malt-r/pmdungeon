package items.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import items.Item;

import java.util.logging.Logger;

public class InventorySelectState implements InventoryControlState {
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  int selectorIdx;

  public InventorySelectState(int selectorIdx) {
    this.selectorIdx = selectorIdx;
  }

  @Override
  public void enter(Inventory inventory) {
    logSelectedItem(inventory);
    printUsage();
  }

  @Override
  public InventoryControlState handleInput(Inventory inventory) {
    InventoryControlState nextState = null;
    if (Gdx.input.isKeyJustPressed((Input.Keys.ESCAPE))) {
      nextState = new OwnInventoryOpenState(this.selectorIdx);
    } else if (Gdx.input.isKeyJustPressed((Input.Keys.Q))) {
      dropItem(inventory);
      nextState = new OwnInventoryOpenState(this.selectorIdx);
    } else if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
      useItem(inventory);
      nextState = new OwnInventoryOpenState(this.selectorIdx);
    } else if (Gdx.input.isKeyJustPressed((Input.Keys.I))) {
      // inspect?
      var item = inventory.getItemAt(this.selectorIdx);
      item.accept(inventory.itemLogger);
    } else if (Gdx.input.isKeyJustPressed((Input.Keys.M))) {
      // swap item positions or put item in bag
    }

    if (openerLeft(inventory)) {
      nextState = new InventoryClosedState();
    }
    return nextState;
  }

  // use the item at current selector index
  private void useItem(Inventory inventory) {
    if (this.selectorIdx < inventory.getCount() && null != inventory.getItemAt(this.selectorIdx)) {
      var item = inventory.removeAt(this.selectorIdx);
      this.selectorIdx -= 1;

      mainLogger.info("using item:");
      item.accept(inventory.itemLogger);
      item.accept(inventory.getOpener());
    }
  }

  // log selected item
  private void logSelectedItem(Inventory inventory) {
    Item item = inventory.getItemAt(this.selectorIdx);
    mainLogger.info("Selected item: " + item.getName());
  }

  // print usage of current state
  private void printUsage() {
    mainLogger.info("Options: ESC (unselect item); Q (drop); I (inspect item); E (use)");
  }

  // drop an item to the location of parent of inventory
  private void dropItem(Inventory inventory) {

    if (this.selectorIdx < inventory.getCount() && null != inventory.getItemAt(this.selectorIdx)) {
      boolean successfullyDropped = inventory.dropItemFromIdx(this.selectorIdx);
      if (successfullyDropped) {
        this.selectorIdx -= 1;
      }
    }
  }
}
