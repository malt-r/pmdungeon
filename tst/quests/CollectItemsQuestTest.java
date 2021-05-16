package quests;
import items.Item;
import main.Hero;
import mock.MockCombatable;
import mock.MockHero;
import mock.MockItem;
import org.junit.Before;
import org.junit.Test;
import quests.CollectItemsQuest;
import quests.KillMonstersQuest;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CollectItemsQuestTest {
    Hero hero;
    ArrayList<Item> mockItems;
    CollectItemsQuest quest;

    @Before
    public void setup() {
        hero = new MockHero();
        mockItems = new ArrayList<Item>();
        mockItems.add(new MockItem());
        quest = new CollectItemsQuest(hero, mockItems, 2);
    }

    @Test
    public void testUpdate() {
        quest.setup();

        var mockItem = new MockItem();
        hero.getInventory().addItem(mockItem);

        assertEquals(quest.getCollected(), 1);
    }

    @Test
    public void testNoUpdate() {
        quest.setup();

        assertEquals(quest.getCollected(), 0);
    }

    @Test
    public void testCleanup() {
        quest.setup();
        quest.cleanup();

        var mockItem = new MockItem();
        hero.getInventory().addItem(mockItem);

        assertEquals(quest.getCollected(), 0);
    }

    @Test
    public void testNotFinished() {
        quest.setup();

        var mockItem = new MockItem();
        for (int i = 0; i < 1; i++) {
            hero.getInventory().addItem(mockItem);
        }

        assertEquals(quest.isFinished(), false);
    }

    @Test
    public void testFinished() {
        quest.setup();

        var mockItem = new MockItem();
        for (int i = 0; i < 3; i++) {
            hero.getInventory().addItem(mockItem);
        }

        assertEquals(quest.isFinished(), true);
    }

    @Test
    public void testReward() {
        quest.setup();

        var mockItem = new MockItem();
        // KillMonsterQuest will at max require 8 kills
        for (int i = 0; i < 3; i++) {
            hero.getInventory().addItem(mockItem);
        }

        var reward = quest.getReward();
        assertNotNull(reward);
    }

    @Test
    public void testLockReward() {
        quest.setup();

        var mockItem = new MockItem();
        // KillMonsterQuest will at max require 8 kills
        for (int i = 0; i < 3; i++) {
            hero.getInventory().addItem(mockItem);
        }

        quest.cleanup();

        var reward = quest.getReward();
        assertNull(reward);
    }

    @Test
    public void testCollectionCount() {
        quest.setup();

        var mockItem = new MockItem();
        var inv = hero.getInventory();
        inv.addItem(mockItem);
        inv.removeAt(0);
        inv.addItem(mockItem);

        assertEquals(quest.getCollected(), 2);
    }

    @Test
    public void testInventoryContentAfterReward() {
        quest.setup();

        var mockItem = new MockItem();
        for (int i = 0; i < 3; i++) {
            hero.getInventory().addItem(mockItem);
        }
        hero.applyReward(quest.getReward());

        assertEquals(hero.getInventory().getCount(), 4);
    }
}
