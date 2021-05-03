package main.sample;

import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.EntityController;
import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController;
import items.ItemFactory;
import items.ItemType;
import monsters.Monster;
import monsters.MonsterFactory;
import monsters.MonsterType;

import java.util.logging.Logger;

public class DebugControl {
  private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public static void SpawnAll(EntityController entityController, LevelController levelController){
    Monster[] monsterArray = new Monster[5];

    for(int i=0;i<monsterArray.length;i++){
      MonsterType monsterType;
      if(i%2==0){
        monsterType = MonsterType.LIZARD;
      }
      else{
        monsterType = MonsterType.DEMON;
      }
      try{
        var mon = MonsterFactory.createMonster(monsterType);
        monsterArray[i]= mon;
        entityController.addEntity(mon);
        //TODO Add which kind of monster spawned
        mainLogger.info("Monster(" + (i + 1) + ") created");
      }
      catch(Exception e){
        mainLogger.severe(e.toString());
      }
    }



    for (Monster monster : monsterArray) {
      monster.setLevel(levelController.getDungeon());
    }


    try {
      var sword = ItemFactory.CreateItem(ItemType.SWORD_REGULAR);
      entityController.addEntity(sword);
      sword.setLevel(levelController.getDungeon());

      var spear = ItemFactory.CreateItem(ItemType.SPEAR_REGULAR);
      entityController.addEntity(spear);
      spear.setLevel(levelController.getDungeon());

      var scroll = ItemFactory.CreateItem(ItemType.SCROLL_SPEED);
      entityController.addEntity(scroll);
      scroll.setLevel(levelController.getDungeon());

      var scrollAttack = ItemFactory.CreateItem(ItemType.SCROLL_ATTACK);
      entityController.addEntity(scrollAttack);
      scrollAttack.setLevel(levelController.getDungeon());

      var potion = ItemFactory.CreateItem(ItemType.POTION_HEAL);
      entityController.addEntity(potion);
      potion.setLevel(levelController.getDungeon());

      var potionPoison = ItemFactory.CreateItem(ItemType.POTION_POISON);
      entityController.addEntity(potionPoison);
      potionPoison.setLevel(levelController.getDungeon());

      var shieldWood = ItemFactory.CreateItem(ItemType.SHIELD_WOOD);
      entityController.addEntity(shieldWood);
      shieldWood.setLevel(levelController.getDungeon());

      var shieldEagle = ItemFactory.CreateItem(ItemType.SHIELD_EAGLE);
      entityController.addEntity(shieldEagle);
      shieldEagle.setLevel(levelController.getDungeon());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void SpawnAllMonster(){

  }

  public void SpawnAllScrolls(){

  }

  public void SpawnAllPotions(){

  }

  public void SpawnAllWeapons(){

  }

  public void SpawnAllShields(){

  }
}
