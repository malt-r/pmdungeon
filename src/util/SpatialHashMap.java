package util;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;
import main.IDrawableEntityObserver;

import java.util.ArrayList;
import java.util.logging.Logger;

import static util.math.Convenience.getFlooredPoint;
import static util.math.Convenience.areFlooredPointsEqual;

// TODO: document
public class SpatialHashMap implements IDrawableEntityObserver {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    final float UPPER_LOADFACTOR_THRESHOLD = 0.8f;
    final float LOWER_LOADFACTOR_THRESHOLD = 0.25f;

    private ArrayList<ArrayList<SpatialHashMapEntry>> buckets;
    private boolean log;

    int filledBuckets;
    public int getFilledBuckets() {
        return filledBuckets;
    }

    public SpatialHashMap(int targetSize) {
        int capacity = findNearestPrime(targetSize);

        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++ ) {
            buckets.add(new ArrayList<SpatialHashMapEntry>());
        }
        this.filledBuckets = 0;
    }

    @Override
    public void update(DrawableEntity entity) {
        updateEntityPosition(entity);
    }

    public void printStats() {
        mainLogger.info("Printing Spatial hashmap stats:");
        mainLogger.info("Filled buckets: " + this.filledBuckets);
        mainLogger.info("Total buckets: " + this.buckets.size());
        mainLogger.info("Loadfactor: " + this.calcLoadFactor());
        mainLogger.info("Max length of collision list: " + getLengthOfLongestCollisionList());
    }

    public void insert(DrawableEntity entity) {
        var key = entity.getPosition();
        int h = hash(key);

        float futureLoadFactor = (float)(this.filledBuckets + 1) / (float)this.buckets.size();
        if (futureLoadFactor > UPPER_LOADFACTOR_THRESHOLD) {
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
            mainLogger.info("Illegal argument exception, ignoring entity " + entity);
        }
    }

    public void clear() {
        for (var entry : this.buckets) {
            entry.clear();
        }
        this.filledBuckets = 0;
    }

    // this is problematic, because the position of the entity may have changed, so it would be found
    // in bucket for entity.getPostition
    // no other way but to search linearly through all entries..
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


    public ArrayList<DrawableEntity> getAt(Point pos) {
        int h = hash(pos);
        ArrayList<DrawableEntity> returnList = new ArrayList<>();

        try {
            var entry = findEntry(h, pos);
            if (entry != null) {
                returnList = new ArrayList<>(entry.getValues());
            }
        } catch (IllegalArgumentException ex) {
            mainLogger.info("IllegalArgumentException, ignoring position " + pos);
        }
        return returnList;
    }

    public ArrayList<DrawableEntity> getInRange(Point lowerBound, Point upperBound) {
        ArrayList<DrawableEntity> returnList = new ArrayList<>();

        for (int x = (int)Math.floor(lowerBound.x);
             x <= (int)Math.floor(upperBound.x);
             x++) {
            for (int y = (int)Math.floor(lowerBound.y);
                 y <= (int)Math.floor(upperBound.y);
                 y++) {
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

    private int hash(Point pos) {
        long flooredX = (long)Math.floor(pos.x);
        long flooredY = (long)Math.floor(pos.y);
        return (int)((flooredX << 8 + flooredY) % this.buckets.size());
    }

    private float calcLoadFactor() {
        return (float)this.filledBuckets / (float)this.buckets.size();
    }

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

    private boolean removeEntityFromEntry(DrawableEntity entity, SpatialHashMapEntry entry) {
        if (null != entry && entry.getValues().contains(entity)) {
            entry.removeFromValues(entity);
            return true;
        }
        return false;
    }

    private SpatialHashMapEntry findEntry(int hash, Point key) throws IllegalArgumentException {
        if (hash < 0) {
            throw new IllegalArgumentException("hash cannot be negative!");
        }

        ArrayList<SpatialHashMapEntry> entriesForHash;
        entriesForHash = this.buckets.get(hash);

        // TODO: remove
        if (entriesForHash.size() != 0) { // search linearly for key
            for (var entry : entriesForHash) {
                if (areFlooredPointsEqual(entry.getKey(), key)) {
                    return entry;
                }
            }
        }

        // key is not in map
        return null;
    }

    private SpatialHashMapEntry insertEntry(int hash, Point key) throws IllegalArgumentException {
        if (hash < 0) {
            throw new IllegalArgumentException("hash cannot be negative!");
        }

        ArrayList<SpatialHashMapEntry> entriesForHash;
        entriesForHash = this.buckets.get(hash);

        for (var entry : entriesForHash) {
            if (areFlooredPointsEqual(entry.getKey(), key)) {
                // entry is already in map
                return entry;
            }
        }
        var newEntry = new SpatialHashMapEntry(getFlooredPoint(key), null);
        entriesForHash.add(newEntry);
        return newEntry;
    }



    private void updateEntityPosition(DrawableEntity entity) {
        boolean noUpdateRequired =
                entity.getLastPosition().equals(DrawableEntity.LAST_POSITION_NOT_SET) ||
                areFlooredPointsEqual(entity.getPosition(), entity.getLastPosition());

        // the entity moved away from the stored cell in the hashmap and should be updated
        if (!noUpdateRequired) {
            // find the cell from which the entity came
            var key = entity.getLastPosition();
            int hash = hash(key);
            var entry = findEntry(hash, key);
            try {
                // TODO: refactor this, this is duplicated code from remove -> should be able to pass key to evaluate
                boolean removedEntity = removeEntityFromEntry(entity, entry);
                if(removedEntity && entry.getValues().size() == 0) {
                    removeEntry(hash, entry);
                }

                insert(entity);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    private int findNearestPrime(int target) {
        boolean foundPrime = false;
        int prime = target;
        while(!foundPrime) {
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
