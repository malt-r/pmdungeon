package util;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;

import java.util.ArrayList;

/** Entry for the SpatialHashMap (stores the DrawableEntities). */
public class SpatialHashMapEntry {
  private final ArrayList<DrawableEntity> values;
  private Point key;

  /**
   * Constructor.
   *
   * @param key The key associated with this entry (The floored point of entities, which will be
   *     stored in this entry)
   * @param values Optional intialization of the stored values, may be null.
   */
  public SpatialHashMapEntry(Point key, ArrayList<DrawableEntity> values) {
    this.key = key;
    if (null != values) {
      this.values = new ArrayList<>(values);
    } else {
      this.values = new ArrayList<>();
    }
  }

  /**
   * Returns the key associated with this entry.
   *
   * @return
   */
  public Point getKey() {
    return key;
  }

  /** Reset this entry (set key to null and clear values). */
  public void reset() {
    this.key = null;
    this.values.clear();
  }

  /**
   * Get the stored DrawableEntities.
   *
   * @return the values.
   */
  public ArrayList<DrawableEntity> getValues() {
    return values;
  }

  /**
   * Remove a DrawableEntity from the stored values.
   *
   * @param entity The DrawableEntity to remove from the stored values.
   */
  public void removeFromValues(DrawableEntity entity) {
    this.values.remove(entity);
  }

  /**
   * Add a new DrawableEntity to the stored values. Will perform a check, if the Entity is already
   * stored in the values.
   *
   * @param entity The new DrawableEntity to store.
   */
  public void addToValues(DrawableEntity entity) {
    if (!this.values.contains(entity)) {
      this.values.add(entity);
    }
  }
}
