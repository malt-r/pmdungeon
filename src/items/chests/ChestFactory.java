package items.chests;


/**
 * Factory for creation of chests
 * <p>
 *     Creates all types of chest that can appear in the game.
 * </p>
 */
public class ChestFactory {
  /**
   *
   * @param   chestType    Type of the chest
   * @throws  Exception   if chestType is not supported
   * @return  A chest, of the specified type.
   */
  public static Chest CreateChest (ChestType chestType) throws Exception{
    if(chestType == ChestType.NORMAL)      return new Chest();
    throw new Exception("ItemType no supported");
  }
}