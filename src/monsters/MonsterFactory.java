package monsters;

import main.Game;
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
   * @param   game        The Game where the monster appears
   * @throws  Exception   if monsterType is not supported
   * @return  A monster, of the specified type.
   */
  public static Monster createMonster(MonsterType monsterType, Game game) throws Exception{
    if(monsterType == MonsterType.DEMON) return new DemonMonster(game);
    if(monsterType == MonsterType.LIZARD) return new LizardMonster(game);
    throw new Exception("MonsterType no supported");
  }
}