package util;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.DrawableEntity;

import java.util.ArrayList;

public class SpatialHashMapEntry {
    private Point key;
    private ArrayList<DrawableEntity> values;

    private boolean isTombstone;

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
        this.isTombstone = false;
        this.key = null;
        this.values.clear();
    }

    public boolean isTombstone() {
        return this.isTombstone;
    }

    public void makeTombstone() {
        this.isTombstone = true;
        this.key = null;
        this.values.clear();
    }

    public ArrayList<DrawableEntity> getValues() {
        return values;
    }

    public void setKey(Point key) {
        this.key = key;
        this.isTombstone = false;
    }

    public void removeFromValues(DrawableEntity entity) {
        this.values.remove(entity);
    }

    public void addToValues(DrawableEntity entity) {
        this.values.add(entity);
        this.isTombstone = false;
    }
}
