package main;

import items.ItemType;
import monsters.MonsterType;
/**
 * Holds information which monsters and items are present in a level.
 * <p>
 *   Contains entities that are present in the level
 * </p>
 */
public class LevelContent {
  public MonsterType[] monsters;
  public ItemType[] items;
  /**
   * Constructor of the LevelContent class.
   * <p>
   *  Instantiates A LevelContent instance
   * </p>
   */
  public LevelContent(MonsterType[] monsters,ItemType[] items) {
    this.monsters= monsters;
    this.items=items;
  }
}
