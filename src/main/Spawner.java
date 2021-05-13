package main;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.EntityController;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import items.Item;
import items.ItemFactory;
import items.ItemType;
import items.chests.Chest;
import items.chests.ChestFactory;
import items.chests.ChestType;
import monsters.Monster;
import monsters.MonsterFactory;
import monsters.MonsterType;
import quests.QuestGiver;
import traps.Trap;
import traps.TrapFactory;
import traps.TrapType;

import java.util.ArrayList;
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
   * @param listToAdd to which list the entities should be added
   * @throws Exception if creation of an itme or monster fails
   */
  public static void spawnEntities(LevelContent levelContent, ArrayList<IEntity> listToAdd) throws Exception {
    for(MonsterType monsterType: levelContent.monsters){
      var monster = MonsterFactory.CreateMonster(monsterType);
      monster.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info(monsterType.toString()+" spawned");
      listToAdd.add(monster);
    }

    for(var itemType:levelContent.items){
      var item = ItemFactory.CreateItem(itemType);
      item.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info(itemType.toString()+" spawned");
      listToAdd.add(item);
    }

    for(var chestType:levelContent.chests){
      var chest = ChestFactory.CreateChest(chestType);
      chest.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info(chestType.toString()+" spawned");
      listToAdd.add(chest);
    }

    for(var trapType:levelContent.traps){
      var trap = TrapFactory.CreateTrap(trapType);
      trap.setLevel(Game.getInstance().getCurrentLevel());
      mainLogger.info(trapType.toString()+" spawned");
      listToAdd.add(trap);
    }

    //QuestGiver
    //TODO - Add spawnQuestGiver method
    var questGiver = new QuestGiver();
    questGiver.setLevel(Game.getInstance().getCurrentLevel());
    mainLogger.info("QuestGiver spawned");
    listToAdd.add(questGiver);
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
