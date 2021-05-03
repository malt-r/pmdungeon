package items.bag;

import items.Item;
import items.inventory.Inventory;
import main.Game;

public class BaseBag<T extends Item> extends Item {
    Inventory<T> inventory;

    public BaseBag(Game game) {
        super(game);
        this.inventory = new Inventory<>(this, 10, game);
    }

    public boolean canAddItem(Item item) {
        return false;
    };

    public boolean addItem(T item) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    protected String getDescription() {
        return null;
    }


}
