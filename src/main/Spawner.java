package main;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import items.Item;
import items.ItemFactory;
import items.ItemType;
import items.chests.Chest;
import items.chests.ChestFactory;
import items.chests.ChestType;
import java.util.ArrayList;
import java.util.logging.Logger;
import monsters.Monster;
import monsters.MonsterFactory;
import monsters.MonsterType;
import quests.QuestGiver;
import traps.Trap;
import traps.TrapFactory;
import traps.TrapType;



/**
 * Spawner class.
 *
 * <p>Utillity class for simple spawning of entities in the game.
 */
public class Spawner {
  private static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * Spawns all monsters and items present in the levelContent in the current game.
   *
   * @param levelContent content of a level
   * @param listToAdd to which list the entities should be added
   * @throws Exception if creation of an itme or monster fails
   */
  public static void spawnEntities(LevelContent levelContent, ArrayList<IEntity> listToAdd)
      throws Exception {
    for (MonsterType monsterType : levelContent.monsters) {
      var monster = MonsterFactory.createMonster(monsterType);
      monster.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info("Monster " + monsterType.toString() + " spawned");
      listToAdd.add(monster);
    }

    for (var itemType : levelContent.items) {
      var item = ItemFactory.createItem(itemType);
      item.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info("Item " + itemType.toString() + " spawned");
      listToAdd.add(item);
    }

    for (var chestType : levelContent.chests) {
      var chest = ChestFactory.createChest(chestType);
      chest.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info("Chest " + chestType.toString() + " spawned");
      listToAdd.add(chest);
    }

    for (var trapType : levelContent.traps) {
      var trap = TrapFactory.createTrap(trapType);
      trap.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info("Trap " + trapType.toString() + " spawned");
      listToAdd.add(trap);
    }

    for (var questGiverType : levelContent.questGivers) {
      var questGiver = new QuestGiver();
      questGiver.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info("Questgiver " + questGiverType.toString() + " spawned");
      listToAdd.add(questGiver);
    }
  }

  /**
   * Spawns a monster of the specific type.
   *
   * @param monsterType of the monster that should be spawned
   * @return spawned monster
   * @throws Exception if monsterType is no supported
   */
  public static Monster spawnMonster(MonsterType monsterType) throws Exception {
    mainLogger.info("Monster " + monsterType.toString() + " spawned");
    return MonsterFactory.createMonster(monsterType);
  }

  /**
   * Spawns a monster of the specific type.
   *
   * @param itemType of the item that should be spawned
   * @return spawned item
   * @throws Exception if itemType is no supported
   */
  public static Item spawnItem(ItemType itemType) throws Exception {
    mainLogger.info("Item " + itemType.toString() + " spawned");
    return ItemFactory.createItem(itemType);
  }

  /**
   * Spawns a trap of the specific type.
   *
   * @param trapType of the trap that should be spawned
   * @return spawned trap
   * @throws Exception if trap is no supported
   */
  public static Trap spawnTrap(TrapType trapType) throws Exception {
    mainLogger.info("Trap " + trapType.toString() + " spawned");
    return TrapFactory.createTrap(trapType);
  }

  /**
   * Spawns a chest of the specific type.
   *
   * @param chestType of the chest that should be spawned
   * @return spawned chest
   * @throws Exception if chest is no supported
   */
  public static Chest spawnChest(ChestType chestType) throws Exception {
    mainLogger.info("Chest " + chestType.toString() + " spawned");
    return ChestFactory.createChest(chestType);
  }
}
