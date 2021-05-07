package main;

import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.EntityController;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController;
import items.ItemFactory;
import monsters.MonsterFactory;
import monsters.MonsterType;

import java.util.logging.Logger;

/**
 * Spawner class.
 * <p>
 *   Utillity class for simple spawning of entities in the game.
 * </p>
 */
public class Spawner {
  private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * Spawns all monsters and items present in the levelContent in the current game-
   *
   * @param levelContent content of a level
   * @param levelController levelController of the game
   * @param entityController entityController of the game
   * @throws Exception if creation of an itme or monster fails
   */
  public static void spawnEntities(LevelContent levelContent, LevelController levelController, EntityController entityController) throws Exception {
    for(MonsterType monsterType: levelContent.monsters){
      var monster = MonsterFactory.createMonster(monsterType);
      mainLogger.info(monsterType.toString()+" spawned");
      entityController.addEntity(monster);
      monster.setLevel(levelController.getDungeon());

    }

    for(var itemType:levelContent.items){
      var item = ItemFactory.CreateItem(itemType);
      mainLogger.info(itemType.toString()+" spawned");
      entityController.addEntity(item);
      item.setLevel(levelController.getDungeon());
    }
  }
}
