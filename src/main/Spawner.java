package main;

import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.EntityController;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController;
import items.ItemFactory;
import monsters.MonsterFactory;
import monsters.MonsterType;

import java.util.logging.Logger;


public class Spawner {
  private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
