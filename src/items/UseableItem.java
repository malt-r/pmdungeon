package items;

public abstract class UseableItem extends Item{

  public UseableItem() {

  }

  @Override
  public boolean isStackable() {
    return true;
  }
}
