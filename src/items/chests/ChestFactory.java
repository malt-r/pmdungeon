package items.chests;

/**
 * Factory for creation of chests
 *
 * <p>Creates all types of chest that can appear in the game.
 */
public class ChestFactory {
  /**
   * Factory for chests.
   *
   * @param chestType Type of the chest
   * @return A chest, of the specified type.
   * @throws Exception if chestType is not supported
   */
  public static Chest createChest(ChestType chestType) throws Exception {
    if (chestType == ChestType.NORMAL) {
      return new Chest();
    }
    throw new Exception("ItemType no supported");
  }
}
