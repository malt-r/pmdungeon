package quests;

import items.Item;
import main.Hero;
import mock.MockHero;
import mock.MockItem;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LevelUpQuestTest {
  Hero hero;
  ArrayList<Item> mockItems;
  LevelUpQuest quest;

  // TODO: check, if rewards were added to heros inventory
  @Before
  public void setup() {
    hero = new MockHero();
    mockItems = new ArrayList<Item>();
    mockItems.add(new MockItem());
    quest = new LevelUpQuest(hero, mockItems, 2);
  }

  @Test
  public void testCleanup() {
    quest.setup();
    quest.cleanup();

    hero.getLevel().increaseXP(100);
    hero.getLevel().increaseXP(1000);

    assertFalse(quest.isFinished());
  }

  @Test
  public void testNotFinished() {
    quest.setup();

    hero.getLevel().increaseXP(100);

    assertEquals(quest.isFinished(), false);
  }

  @Test
  public void testFinished() {
    quest.setup();

    // this should level at least two levels..
    hero.getLevel().increaseXP(100);
    hero.getLevel().increaseXP(1000);

    assertEquals(quest.isFinished(), true);
  }

  @Test
  public void testReward() {
    quest.setup();

    hero.getLevel().increaseXP(100);
    hero.getLevel().increaseXP(1000);

    var reward = quest.getReward();
    assertNotNull(reward);
  }

  @Test
  public void testLockReward() {
    quest.setup();

    hero.getLevel().increaseXP(100);
    hero.getLevel().increaseXP(1000);
    quest.cleanup();

    var reward = quest.getReward();
    assertNull(reward);
  }
}
