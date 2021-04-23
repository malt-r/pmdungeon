package monsters;

import main.Game;

import java.util.Locale;

public class MonsterFactory {
  public static Monster createMonster(String monsterType, Game game) throws Exception {

    if(monsterType.equals("demon")) return new DemonMonster(game);

    if(monsterType.equals("lizard")) return new LizardMonster(game);

    throw new Exception("Unknown monsterType");
  }
}
