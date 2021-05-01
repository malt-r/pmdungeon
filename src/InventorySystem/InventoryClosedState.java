package InventorySystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InventoryClosedState implements IInventoryControlState {
    @Override
    public IInventoryControlState handleInput(Inventory inventory) {
        IInventoryControlState nextState = null;
        // TODO: how to prevent all inventories (from chests and stuff) to pop open, if
        // I is pressed? -> this needs to be in hero
        if (Gdx.input.isKeyJustPressed((Input.Keys.I))){
            nextState = new InventoryOpenState();
        }
        return nextState;
    }
}
