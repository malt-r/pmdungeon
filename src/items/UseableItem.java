package items;

import main.Game;

public abstract class UseableItem extends Item{

  public UseableItem(Game game) {
    super(game);
  }

  @Override
  public boolean isStackable() {
    return true;
  }
}
