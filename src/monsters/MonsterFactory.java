package monsters;

/**
 * Factory for creation of monster
 * <p>
 *     Creates all types of monster that can appear in the game.
 * </p>
 */
public class MonsterFactory {
  /**
   * Normalize the difference-vector between two Points on a defined basis.
   *
   * @param   monsterType Type of the monster
   * @throws  Exception   if monsterType is not supported
   * @return  A monster, of the specified type.
   */
  public static Monster CreateMonster(MonsterType monsterType) throws Exception{
    if(monsterType == MonsterType.DEMON) return new DemonMonster();
    if(monsterType == MonsterType.LIZARD) return new LizardMonster();
    if(monsterType == MonsterType.CHEST) return new ChestMonster();
    throw new Exception("MonsterType no supported");
  }
}