package main;

import items.ItemType;
import monsters.MonsterType;

public class LevelContent {
  public MonsterType[] monsters;
  public ItemType[] items;

  public LevelContent(MonsterType[] monsters,ItemType[] items) {
    this.monsters= monsters;
    this.items=items;
  }
}
