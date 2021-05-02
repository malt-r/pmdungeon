package items.inventory;

public class InventoryClosedState implements IInventoryControlState {

    // TODO: find real usage for this..
    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        // TODO: how to prevent all inventories (from chests and stuff) to pop open, if
        // I is pressed? -> this needs to be in hero
        //if (Gdx.input.isKeyJustPressed((Input.Keys.I))){
            ///nextState = new InventoryOpenState();
        //}
        return nextState;
    }
}
