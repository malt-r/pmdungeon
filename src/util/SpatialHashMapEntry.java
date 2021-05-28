package util;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;

import java.util.ArrayList;

public class SpatialHashMapEntry {
    private Point key;
    private ArrayList<DrawableEntity> values;

    public SpatialHashMapEntry(Point key, ArrayList<DrawableEntity> values) {
        this.key = key;
        if (null != values) {
            this.values = new ArrayList<>(values);
        } else {
            this.values = new ArrayList<>();
        }
    }

    public Point getKey() {
        return key;
    }

    public void reset() {
        this.key = null;
        this.values.clear();
    }

    public ArrayList<DrawableEntity> getValues() {
        return values;
    }

    public void setKey(Point key) {
        this.key = key;
    }

    public void removeFromValues(DrawableEntity entity) {
        this.values.remove(entity);
    }

    public void addToValues(DrawableEntity entity) {
        if (!this.values.contains(entity)){
            this.values.add(entity);
        }
    }
}
