package main;

import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.EntityController;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController;
import items.Item;
import items.ItemFactory;
import items.ItemType;
import items.chests.Chest;
import items.chests.ChestFactory;
import items.chests.ChestType;
import monsters.Monster;
import monsters.MonsterFactory;
import monsters.MonsterType;
import traps.Trap;
import traps.TrapFactory;
import traps.TrapType;

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
      var monster = MonsterFactory.CreateMonster(monsterType);
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

    for(var chestType:levelContent.chests){
      var chest = ChestFactory.CreateChest(chestType);
      mainLogger.info(chestType.toString()+" spawned");
      entityController.addEntity(chest);
      chest.setLevel(levelController.getDungeon());
    }

    for(var trapType:levelContent.traps){
      var trap = TrapFactory.CreateTrap(trapType);
      mainLogger.info(trapType.toString()+" spawned");
      entityController.addEntity(trap);
      trap.setLevel(levelController.getDungeon());
    }
  }

  /**
   * Spawns a monster of the specific type
   * @param monsterType of the monster that should be spawned
   * @return  spawned monster
   * @throws Exception  if monsterType is no supported
   */
  public static Monster spawnMonster(MonsterType monsterType) throws Exception {
    mainLogger.info(monsterType.toString()+" spawned");
    return MonsterFactory.CreateMonster(monsterType);
  }
  /**
   * Spawns a monster of the specific type
   * @param itemType of the item that should be spawned
   * @return  spawned item
   * @throws Exception  if itemType is no supported
   */
  public static Item spawnItem(ItemType itemType) throws Exception {
    mainLogger.info(itemType.toString()+" spawned");
    return ItemFactory.CreateItem(itemType);
  }
  /**
   * Spawns a trap of the specific type
   * @param trapType of the trap that should be spawned
   * @return  spawned trap
   * @throws Exception  if trap is no supported
   */
  public static Trap spawnTrap(TrapType trapType) throws Exception {
    mainLogger.info(trapType.toString()+" spawned");
    return TrapFactory.CreateTrap(trapType);
  }

  /**
   * Spawns a chest of the specific type
   * @param chestType of the chest that should be spawned
   * @return  spawned chest
   * @throws Exception  if chest is no supported
   */
  public static Chest spawnChest(ChestType chestType) throws Exception {
    mainLogger.info(chestType.toString()+" spawned");
    return ChestFactory.CreateChest(chestType);
  }
}
