package items;

public abstract class EquipableItem extends Item{
  public EquipableItem() {
    super();
  }

  @Override
  public boolean isStackable() {
    return false;
  }
}
