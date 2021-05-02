package items.shields;

import items.EquipableItem;
import main.Game;

public abstract class Shield extends EquipableItem {
  protected int defenseValue;
  public Shield(Game game) {
    super(game);
  }
  public int getDefenseValue(){
    return defenseValue;
  }
}
