package main;

import items.ItemType;
import items.chests.ChestType;
import monsters.MonsterType;
import quests.QuestGiverType;
import traps.TrapType;

/**
 * Holds information which monsters and items are present in a level.
 * <p>
 *   Contains entities that are present in the level
 * </p>
 */
public class LevelContent {
  public MonsterType[] monsters;
  public ItemType[] items;
  public ChestType[] chests;
  public TrapType[] traps;
  public QuestGiverType[] questGivers;
  /**
   * Constructor of the LevelContent class.
   * <p>
   *  Instantiates A LevelContent instance
   * </p>
   */
  public LevelContent(MonsterType[] monsters, ItemType[] items, ChestType[] chestTypes, TrapType[] trapType, QuestGiverType[] questGiverType) {
    this.monsters= monsters;
    this.items=items;
    this.chests= chestTypes;
    this.traps= trapType;
    this.questGivers = questGiverType;
  }
}
