package main;

import items.ItemType;
import items.chests.ChestType;
import java.util.ArrayList;
import monsters.MonsterType;
import quests.QuestGiverType;
import traps.TrapType;



/**
 * Class for holding and acessing all LevelContents.
 *
 * <p>Contains everything that describes an item.
 */
public class LevelInfo {
  private final ArrayList<LevelContent> content;

  /**
   * Constructor of the LevelInfo class.
   *
   * <p>This constructor will instantieates all levelContent with the right data.
   */
  public LevelInfo() {
    content = new ArrayList<>();
    var level1 =
        new LevelContent(
            new MonsterType[] {MonsterType.LIZARD, MonsterType.CHEST},
            new ItemType[] {ItemType.SWORD_REGULAR, ItemType.SCROLL_SUPERVISION, ItemType.STAFF},
            new ChestType[] {ChestType.NORMAL},
            new TrapType[] {TrapType.HOLE, TrapType.SPIKES, TrapType.ACTIVATOR, TrapType.TELEPORT},
            new QuestGiverType[] {QuestGiverType.DEFAULT});
    content.add(level1);
    var level2 =
        new LevelContent(
            new MonsterType[] {MonsterType.DEMON},
            new ItemType[] {ItemType.SWORD_GOLD},
            new ChestType[] {ChestType.NORMAL, ChestType.NORMAL},
            new TrapType[] {TrapType.HOLE, TrapType.SPIKES, TrapType.ACTIVATOR},
            new QuestGiverType[] {QuestGiverType.DEFAULT});
    content.add(level2);
    var level3 =
        new LevelContent(
            new MonsterType[] {MonsterType.LIZARD},
            new ItemType[] {ItemType.POTION_HEAL},
            new ChestType[] {ChestType.NORMAL, ChestType.NORMAL},
            new TrapType[] {TrapType.HOLE, TrapType.SPIKES, TrapType.ACTIVATOR},
            new QuestGiverType[] {QuestGiverType.DEFAULT});
    content.add(level3);
    var level4 =
        new LevelContent(
            new MonsterType[] {MonsterType.LIZARD},
            new ItemType[] {ItemType.POTION_POISON},
            new ChestType[] {ChestType.NORMAL, ChestType.NORMAL},
            new TrapType[] {TrapType.HOLE, TrapType.SPIKES, TrapType.ACTIVATOR},
            new QuestGiverType[] {QuestGiverType.DEFAULT});
    content.add(level4);
    var level5 =
        new LevelContent(
            new MonsterType[] {MonsterType.LIZARD},
            new ItemType[] {ItemType.SCROLL_ATTACK},
            new ChestType[] {ChestType.NORMAL, ChestType.NORMAL},
            new TrapType[] {TrapType.HOLE, TrapType.SPIKES, TrapType.ACTIVATOR},
            new QuestGiverType[] {QuestGiverType.DEFAULT});
    content.add(level5);
  }

  /**
   * Returns a specific LevelContent instance which holds information which items and monsters are
   * present in a specific level.
   *
   * @param index index of the level
   * @return a LevelContent instance with information of a level
   */
  public LevelContent getLevelContent(int index) {
    return content.get(index);
  }
}
