package items.bag;

import items.Item;
import items.weapons.Weapon;
import main.Game;

public class WeaponBag<T extends Weapon> extends BaseBag<T>{
    public WeaponBag(Game game) {
        super(game);
    }

    @Override
    public boolean canAddItem(Item item) {
        if (item instanceof Weapon)
            return true;
        else
            return false;
    }

    @Override
    public boolean addItem(T item) {
        this.inventory.addItem(item);
        return true;
    }
}
