package quests;
import items.Item;
import main.Hero;
import mock.MockCombatable;
import mock.MockHero;
import mock.MockItem;
import org.junit.Before;
import org.junit.Test;
import quests.KillMonstersQuest;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class KillMonstersQuestTest{
    Hero hero;
    ArrayList<Item> mockItems;
    KillMonstersQuest quest;

    @Before
    public void setup() {
        hero = new MockHero();
        mockItems = new ArrayList<Item>();
        mockItems.add(new MockItem());
        quest = new KillMonstersQuest(hero, mockItems);
    }

    @Test
    public void testUpdate() {
        quest.setup();

        var mockCombatable = new MockCombatable();
        hero.attack(mockCombatable);

        assertEquals(quest.getKilled(), 1);
    }

    @Test
    public void testNoUpdate() {
        quest.setup();

        assertEquals(quest.getKilled(), 0);
    }

    @Test
    public void testCleanup() {
        quest.setup();
        quest.cleanup();

        var mockCombatable = new MockCombatable();
        hero.attack(mockCombatable);

        assertEquals(quest.getKilled(), 0);
    }

    @Test
    public void testNotFinished() {
        quest.setup();

        var mockCombatable = new MockCombatable();
        // KillMonsterQuest will at min require 3 kills
        for (int i = 0; i < 1; i++) {
            hero.attack(mockCombatable);
        }

        assertEquals(quest.isFinished(), false);
    }

    @Test
    public void testFinished() {
        quest.setup();

        var mockCombatable = new MockCombatable();
        // KillMonsterQuest will at max require 8 kills
        for (int i = 0; i < 9; i++) {
            hero.attack(mockCombatable);
        }

        assertEquals(quest.isFinished(), true);
    }

    @Test
    public void testReward() {
        quest.setup();

        var mockCombatable = new MockCombatable();
        // KillMonsterQuest will at max require 8 kills
        for (int i = 0; i < 9; i++) {
            hero.attack(mockCombatable);
        }

        var reward = quest.getReward();
        assertNotNull(reward);
    }

    @Test
    public void testLockReward() {
        quest.setup();

        var mockCombatable = new MockCombatable();
        // KillMonsterQuest will at max require 8 kills
        for (int i = 0; i < 9; i++) {
            hero.attack(mockCombatable);
        }

        quest.cleanup();

        var reward = quest.getReward();
        assertNull(reward);
    }
}
