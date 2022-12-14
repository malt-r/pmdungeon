package main;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import mock.MockHero;
import mock.MockMonster;
import org.junit.Before;
import org.junit.Test;
import util.SpatialHashMap;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class CombatableTest {
  Hero hero;
  MockMonster monster1;
  MockMonster monster2;
  ArrayList<IEntity> entities;

  // This does actually not test the in range functionality,
  // but the correct finding of targets using a spatial hashmap
  Function<Point, Boolean> heroInRange =
      (p) -> {
        var floorx1 = Math.floor(p.x);
        var floory1 = Math.floor(p.y);
        var floorxHero = Math.floor(hero.getPosition().x);
        var flooryHero = Math.floor(hero.getPosition().y);
        return floorx1 == floorxHero && floory1 == flooryHero;
      };

  @Before
  public void setup() {
    hero = new MockHero(new Point(1.4f, 1.4f));
    monster1 = new MockMonster(new Point(1.2f, 1.2f));
    monster2 = new MockMonster(new Point(4.2f, 4.2f));

    entities = new ArrayList<>();
    entities.add(hero);
    entities.add(monster1);
    entities.add(monster2);
  }

  @Test
  public void testFindTargetIfReachableLambda() {
    BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder =
        (p1, p2) -> {
          ArrayList<DrawableEntity> drawableEntities = new ArrayList<>();
          for (var entity : entities) {
            if (entity instanceof DrawableEntity) {
              drawableEntities.add((DrawableEntity) entity);
            }
          }
          return drawableEntities;
        };

    hero.attackTargetIfReachable(hero.getPosition(), heroInRange, entityFinder);
    var target = hero.getTarget();

    assertEquals(monster1, target);
  }

  @Test
  public void testFindTargetIfReachableSpatialHashMap() {
    SpatialHashMap shm = new SpatialHashMap(100);
    shm.insert(hero);
    shm.insert(monster1);
    shm.insert(monster2);

    BiFunction<Point, Point, ArrayList<DrawableEntity>> entityFinder =
        (p1, p2) -> {
          return shm.getInRange(p1, p2);
        };

    hero.attackTargetIfReachable(hero.getPosition(), heroInRange, entityFinder);
    var target = hero.getTarget();

    assertEquals(monster1, target);
  }
}
