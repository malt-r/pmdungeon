package util;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Hero;
import mock.MockHero;
import mock.MockMonster;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpatialHashMapTest {
  Hero hero;
  MockMonster monster1;
  MockMonster monster2;
  SpatialHashMap shm;

  @Before
  public void setup() {
    hero = new MockHero(new Point(1.4f, 1.4f));
    monster1 = new MockMonster(new Point(1.2f, 1.2f));
    monster2 = new MockMonster(new Point(4.2f, 4.2f));

    shm = new SpatialHashMap(10);
  }

  @Test
  public void testInsert() {
    shm.insert(hero);

    assertEquals(shm.getFilledBuckets(), 1);
  }

  @Test
  public void testGetAtPointPositive() {
    shm.insert(hero);

    var entries = shm.getAt(hero.getPosition());

    assertTrue(entries.contains(hero));
  }

  @Test
  public void testGetAtPointNegative() {
    shm.insert(hero);

    var entries = shm.getAt(new Point(5.0f, 5.0f));

    assertFalse(entries.contains(hero));
  }

  @Test
  public void testGetInRangeAll() {
    shm.insert(hero);
    shm.insert(monster1);
    shm.insert(monster2);

    var entries = shm.getInRange(new Point(1.0f, 1.0f), new Point(4.0f, 4.0f));

    assertEquals(entries.size(), 3);
  }

  @Test
  public void testGetInRangePartial() {
    shm.insert(hero);
    shm.insert(monster1);
    shm.insert(monster2);

    var entries = shm.getInRange(new Point(1.0f, 1.0f), new Point(3.0f, 3.0f));

    assertEquals(entries.size(), 2);
  }

  @Test
  public void testRemoveNegative() {
    shm.insert(hero);
    shm.remove(monster1);

    assertEquals(shm.getFilledBuckets(), 1);
  }

  @Test
  public void testRemovePositive() {
    shm.insert(hero);
    shm.remove(hero);

    assertEquals(shm.getFilledBuckets(), 0);
  }

  @Test
  public void testRemoveWithOtherEntriesPositive() {
    shm.insert(hero);
    shm.insert(monster1);
    shm.insert(monster2);
    shm.remove(hero);

    assertEquals(shm.getFilledBuckets(), 2);
  }
}
