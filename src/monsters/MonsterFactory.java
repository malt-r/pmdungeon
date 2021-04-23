package monsters;

import main.Game;

public class MonsterFactory {
  public static Monster createMonster(MonsterType monsterType, Game game) throws Exception{
    if(monsterType == MonsterType.DEMON) return new DemonMonster(game);
    if(monsterType == MonsterType.LIZARD) return new LizardMonster(game);
    throw new Exception("MonsterType no supported");
  }
}
