package items.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Open state.
 */
public class OwnInventoryOpenState extends InventoryOpenState {

  public OwnInventoryOpenState() {
    this.selectorIdx = -1;
  }

  public OwnInventoryOpenState(int selectorIdx) {
    this.selectorIdx = selectorIdx;
  }

  @Override
  public IInventoryControlState handleInput(Inventory inventory) {
    var nextState = super.handleInput(inventory);

    if (null == nextState && !lockInput) {
      if (Gdx.input.isKeyJustPressed((Input.Keys.E))) {
        if (this.selectorIdx >= 0) {
          nextState = new InventorySelectState(this.selectorIdx);
          notifyObservers();
        } else {
          mainLogger.info("can not select anything, inventory is empty");
        }
      }
    }
    return nextState;
  }

  @Override
  protected void printUsage() {
    super.printUsage();
    mainLogger.info("E (select item)");
  }
}
