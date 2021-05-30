package util;

import static util.math.Convenience.areFlooredPointsEqual;
import static util.math.Convenience.getFlooredPoint;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import java.util.ArrayList;
import java.util.logging.Logger;
import main.DrawableEntity;
import main.IDrawableEntityObserver;



/**
 * Hashmap implementation, which stores DrawableEntities with their positions as keys. Uses chaining
 * for collision resolution.
 *
 * <p>The Hashmap does not store the DrawableEntities directly but stores SpatialHashMapEntries,
 * which in turn store multiple DrawableEntities for on position (so that multiple DrawableEntities
 * can be stored for the same position)
 */
public class SpatialHashMap implements IDrawableEntityObserver {
  /** Logger. */
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  final float upperLoadfactorThreshold = 0.8f;

  // buckets of the hashmap
  private final ArrayList<ArrayList<SpatialHashMapEntry>> buckets;

  // log or don't log. for debugging
  private final boolean log = false;

  // keep track of the amount of filled buckets
  int filledBuckets;

  /**
   * Constructor.
   *
   * @param targetSize The number of buckets, which should at least be contained in this hashmap.
   *     Will be set to next bigger prime.
   */
  public SpatialHashMap(int targetSize) {
    int capacity = findNearestPrime(targetSize);

    buckets = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      buckets.add(new ArrayList<>());
    }
    this.filledBuckets = 0;
  }

  /** return the number of filled buckets. */
  public int getFilledBuckets() {
    return filledBuckets;
  }

  /**
   * The position of an observed entity was updated, therefore the position of the entity in the map
   * may need to be updated.
   *
   * @param entity The entity, whichs position was updated.
   */
  @Override
  public void update(DrawableEntity entity) {
    updateEntityPosition(entity);
  }

  /**
   * Print info about this hashmap (number of filled buckets, total bucket, loadfactor, length of
   * longest collision length".
   */
  public void printStats() {
    mainLogger.info("Printing Spatial hashmap stats:");
    mainLogger.info("Filled buckets: " + this.filledBuckets);
    mainLogger.info("Total buckets: " + this.buckets.size());
    mainLogger.info("Loadfactor: " + this.calcLoadFactor());
    mainLogger.info("Max length of collision list: " + getLengthOfLongestCollisionList());
  }

  /**
   * Insert a new entity in the hashmap.
   *
   * @param entity The entity to insert.
   */
  public void insert(DrawableEntity entity) {
    var key = entity.getPosition();
    int h = hash(key);

    float futureLoadFactor = (float) (this.filledBuckets + 1) / (float) this.buckets.size();
    if (futureLoadFactor > upperLoadfactorThreshold) {
      // TODO: grow
    }

    try {
      var entry = findEntry(h, key);
      if (entry == null) {
        this.filledBuckets++;
        entry = insertEntry(h, key);
      }

      if (log) {
        mainLogger.info("inserted " + entity + " for bucket of key (" + key.x + "|" + key.y + ")");
      }

      entry.addToValues(entity);
      entity.register(this);
    } catch (IllegalArgumentException ex) {
      if (log) {
        mainLogger.info("Illegal argument exception, ignoring entity " + entity);
      }
    }
  }

  /** Clear the whole map, remove all entries. */
  public void clear() {
    for (var entry : this.buckets) {
      entry.clear();
    }
    this.filledBuckets = 0;
  }

  /**
   * Remove an entry from this hashmap.
   *
   * @param entity The entity to remove.
   * @return True, if the entity was removed successfully. False otherwise.
   */
  public boolean remove(DrawableEntity entity) {
    SpatialHashMapEntry entry;
    var key = entity.getPosition();
    int hash = hash(entity.getPosition());
    entry = findEntry(hash, key);

    boolean removedEntry = removeEntityFromEntry(entity, entry);
    if (removedEntry && entry.getValues().size() == 0) { // remove entry
      entity.unregister(this);
      removeEntry(hash, entry);
    }
    return removedEntry;
  }

  /**
   * Get all entities which are stored in the bucket for a specified Point. The components of the
   * Point will be floored.
   *
   * @param pos The Point to get the stored entities for.
   * @return An ArrayList with all entities in the bucket specified by pos.
   */
  public ArrayList<DrawableEntity> getAt(Point pos) {
    int h = hash(pos);
    ArrayList<DrawableEntity> returnList = new ArrayList<>();

    try {
      var entry = findEntry(h, pos);
      if (entry != null) {
        // copy entries to new ArrayList to not risk concurrent modification exception, if client
        // code updates
        // positions of some of the entities.
        returnList = new ArrayList<>(entry.getValues());
      }
    } catch (IllegalArgumentException ex) {
      if (log) {
        mainLogger.info("IllegalArgumentException, ignoring position " + pos);
      }
    }
    return returnList;
  }

  /**
   * Get all entities which are stored in the buckets for a specified range. The range is specified
   * by the lowerBound and upperBound (lower and upper bound for x and y coordinates).
   *
   * @param lowerBound The lower bound of the range (lower bound for x and y coordinates).
   * @param upperBound The upper bound of the range (upper bound for x and y coordinates).
   * @return array list of entities
   */
  public ArrayList<DrawableEntity> getInRange(Point lowerBound, Point upperBound) {
    ArrayList<DrawableEntity> returnList = new ArrayList<>();

    for (int x = (int) Math.floor(lowerBound.x); x <= (int) Math.floor(upperBound.x); x++) {
      for (int y = (int) Math.floor(lowerBound.y); y <= (int) Math.floor(upperBound.y); y++) {
        returnList.addAll(getAt(new Point(x, y)));
      }
    }
    return returnList;
  }

  // TODO: find efficient way to calculate cells to return;
  // maybe just sweep one quarter circle  in 45 deg increment and calculate remaining coords
  public ArrayList<DrawableEntity> getInRadius(Point center, float radius) {
    return new ArrayList<>();
  }

  // calculate hash for Point (shift floored x coordinate 8 to left and add floored y coordinate)
  private int hash(Point pos) {
    long flooredX = (long) Math.floor(pos.x);
    long flooredY = (long) Math.floor(pos.y);
    return (int) ((flooredX << 8 + flooredY) % this.buckets.size());
  }

  // calculate the load factor (currently filled buckets / total bucket count)
  private float calcLoadFactor() {
    return (float) this.filledBuckets / (float) this.buckets.size();
  }

  // remove an entry from a bucket.
  private boolean removeEntry(int hash, SpatialHashMapEntry entry) {
    var bucket = this.buckets.get(hash);
    if (bucket.size() > 0) {
      if (bucket.remove(entry)) {
        this.filledBuckets--;
        return true;
      }
    }
    return false;
  }

  // remove a DrawableEntity from an SpatialHashMapEntry
  private boolean removeEntityFromEntry(DrawableEntity entity, SpatialHashMapEntry entry) {
    if (null != entry && entry.getValues().contains(entity)) {
      entry.removeFromValues(entity);
      return true;
    }
    return false;
  }

  // find entry with key at position specified by hash, will throw IllegalArgumentException, if the
  // hash is negative
  private SpatialHashMapEntry findEntry(int hash, Point key) throws IllegalArgumentException {
    if (hash < 0) {
      throw new IllegalArgumentException("hash cannot be negative!");
    }

    ArrayList<SpatialHashMapEntry> entriesForHash;
    entriesForHash = this.buckets.get(hash);

    for (var entry : entriesForHash) {
      if (areFlooredPointsEqual(entry.getKey(), key)) {
        return entry;
      }
    }

    // key is not in map
    return null;
  }

  // insert a new entry with key at position specified by hash, will throw IllegalArgumentException,
  // if the
  // hash is negative
  private SpatialHashMapEntry insertEntry(int hash, Point key) throws IllegalArgumentException {
    var entry = findEntry(hash, key);
    if (entry != null) { // entry is already in map
      return entry;
    }

    // add new entry
    var newEntry = new SpatialHashMapEntry(getFlooredPoint(key), null);
    var entriesForHash = this.buckets.get(hash);
    entriesForHash.add(newEntry);
    return newEntry;
  }

  // update the position of a DrawableEntity in the hashmap
  private void updateEntityPosition(DrawableEntity entity) {
    // determine, if the position needs to be updated (only if the floored components of current
    // position and last
    // position dont match
    boolean noUpdateRequired =
        entity.getLastPosition().equals(DrawableEntity.LAST_POSITION_NOT_SET)
            || areFlooredPointsEqual(entity.getPosition(), entity.getLastPosition());

    // the entity moved away from the stored cell in the hashmap and should be updated
    if (!noUpdateRequired) {
      // find the cell from which the entity came
      var key = entity.getLastPosition();
      int hash = hash(key);
      var entry = findEntry(hash, key);
      try {
        // TODO: refactor this, this is duplicated code from remove -> should be able to pass key to
        // evaluate
        boolean removedEntity = removeEntityFromEntry(entity, entry);
        if (removedEntity && entry.getValues().size() == 0) {
          removeEntry(hash, entry);
        }

        insert(entity);
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      }
    }
  }

  // find next biggest prime after target
  private int findNearestPrime(int target) {
    boolean foundPrime = false;
    int prime = target;
    while (!foundPrime) {
      for (prime = target; ; prime++) {
        for (int i = 2; i < 1 + Math.sqrt(prime); i++) {
          if (prime % i == 0) {
            break;
          }
          foundPrime = true;
        }
        if (foundPrime) {
          break;
        }
      }
    }
    return prime;
  }

  // iterate over all buckets and store maximum length of chaining list
  private int getLengthOfLongestCollisionList() {
    int maxLength = 0;
    for (var entry : this.buckets) {
      if (entry.size() > maxLength) {
        maxLength = entry.size();
      }
    }
    return maxLength;
  }
}
