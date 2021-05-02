package items;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;

public abstract class EquipableItem extends Item{
  public EquipableItem(Game game) {
    super(game);
  }

  @Override
  public boolean isStackable() {
    return false;
  }
}
