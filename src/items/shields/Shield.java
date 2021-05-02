package items.shields;

import items.EquipableItem;
import main.Game;

public abstract class Shield extends EquipableItem {
  protected float defenseValue;
  public Shield(Game game) {
    super(game);
  }
  public float getDefenseValue(){
    return defenseValue;
  }
}
