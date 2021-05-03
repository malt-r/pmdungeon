package main;

import items.ItemType;
import monsters.MonsterType;
import java.util.ArrayList;

public class LevelInfo {
  private final ArrayList<LevelContent> content;
  public LevelInfo(){
    content = new ArrayList<>();
    var level1 = new LevelContent(new MonsterType[]{MonsterType.DEMON},new ItemType[]{ItemType.SWORD_REGULAR});
    content.add(level1);
  }

  public LevelContent getLevelContent(int index){
    return content.get(index);
  }
}
