package main;

import items.ItemType;
import monsters.MonsterType;
import java.util.ArrayList;
/**
 * Class for holding and acessing all LevelContents.
 * <p>
 *   Contains everything that describes an  item.
 * </p>
 */
public class LevelInfo {
  private final ArrayList<LevelContent> content;
  /**
   * Constructor of the LevelInfo class.
   * <p>
   * This constructor will instantieates all levelContent with the right data.
   * </p>
   */
  public LevelInfo(){
    content = new ArrayList<>();
    var level1 = new LevelContent(new MonsterType[]{MonsterType.DEMON},new ItemType[]{ItemType.SWORD_REGULAR});
    content.add(level1);
    var level2 = new LevelContent(new MonsterType[]{MonsterType.LIZARD},new ItemType[]{ItemType.SPEAR_REGULAR});
    content.add(level2);
    var level3 = new LevelContent(new MonsterType[]{MonsterType.LIZARD},new ItemType[]{ItemType.POTION_HEAL});
    content.add(level3);
    var level4 = new LevelContent(new MonsterType[]{MonsterType.LIZARD},new ItemType[]{ItemType.POTION_POISON});
    content.add(level4);
    var level5 = new LevelContent(new MonsterType[]{MonsterType.LIZARD},new ItemType[]{ItemType.SCROLL_ATTACK});
    content.add(level5);
  }

  /**
   * Returns a specific LevelContent instance which holds information which
   * items and monsters are present in a specific level.
   * @param index index of the level
   * @return a LevelContent instance with information of a level
   */
  public LevelContent getLevelContent(int index){
    return content.get(index);
  }
}
