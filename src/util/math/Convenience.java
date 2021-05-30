package util.math;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.tiles.Tile;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/**
 * Collection of various math-related convenience functions.
 */
public class Convenience {

    /**
     * Scale the difference-vector between two Points.
     *
     * @param p1                 The origin of the vector
     * @param p2                 The tip of the vector
     * @param scale The scale which should be applied to the difference vector.
     * @return A Vec of the length specified by scale
     */
    static public Vec scaleDelta(Vec p1, Vec p2, float scale) {
        Vec diff = p2.subtract(p1);
        return diff.scale(scale);
    }

    /**
     * Scale the difference-vector between two Points.
     *
     * @param p1                 The origin of the vector
     * @param p2                 The tip of the vector
     * @param scale The scale which should be applied to the difference vector.
     * @return A Vec of the length specified by scale
     */
    static public Vec scaleDelta(Point p1, Point p2, float scale) {
        return scaleDelta(new Vec(p1), new Vec(p2), scale);
    }

    /**
     * Get random integer between two integers.
     * @param min lower bound
     * @param max upper bound
     * @return integer between min and max
     */
    static public int getRandBetween(int min, int max) {
        return (int)((Math.random() * (max - min)) + min);
    }

    /**
     * Floor the components of a point.
     * @param toFloor The point of which the components are to be floored.
     * @return Point toFloor in a floored verison.
     */
    static public Point getFlooredPoint(Point toFloor) {
        return new Point((float)Math.floor(toFloor.x), (float)Math.floor(toFloor.y));
    }

    /**
     * Floor the components of the passed points and check, if they are equal.
     * @param p1 first point.
     * @param p2 second point.
     * @return True, if the floored components of the points are equal.
     */
    static public boolean areFlooredPointsEqual(Point p1, Point p2) {
        if (Math.floor(p1.x) == Math.floor(p2.x) &&
                Math.floor(p1.y) == Math.floor(p2.y)) {
            return true;
        }
        return false;
    }

    /**
     * Check, if two drawables are in intersecting range (by default 1.0f).
     * @param drawable1 first drawable
     * @param drawable2 second drawable
     * @return          if the two drawables are intersecting
     */
    static public boolean checkForIntersection (IDrawable drawable1, IDrawable drawable2) {
        return checkForIntersection(drawable1, drawable2, 1.0f);
    }

    /**
     * Check, if two Points are in intersecting range (by default 1.0f).
     * @param p1 first point.
     * @param p2 second point.
     * @param maxDiff The maximum difference between the two points which qualifies as "intersecting range"
     * @return          if the two Points are in intersecting range
     */
    static public boolean checkForIntersection (Point p1, Point p2, float maxDiff) {
        var diff = new Vec(p1).subtract(new Vec(p2)).magnitude();
        return diff < maxDiff;
    }

    /**
     * Check, if two Points are in intersecting range (by default 1.0f).
     * @param p1 first point.
     * @param p2 second point.
     * @return          if the two Points are in intersecting range
     */
    static public boolean checkForIntersection (Point p1, Point p2) {
        return checkForIntersection(p1, p2, 1.0f);
    }

    /**
     * Check, if two drawables are in intersecting range (by default 1.0f).
     * @param drawable1 the first drawable.
     * @param drawable2 the second drawable.
     * @param maxDiff The maximum difference between the two drawables which qualifies as "intersecting range"
     * @return          if the two Points are in intersecting range
     */
    static public boolean checkForIntersection (IDrawable drawable1, IDrawable drawable2, float maxDiff) {
        var diff = new Vec(drawable1.getPosition()).subtract(new Vec(drawable2.getPosition())).magnitude();
        return diff < maxDiff;
    }

    /**
     * Gets the tile which has the corosponding point in it.
     * @param p Point to check
     * @param level level which has the tiles
     * @return Tile which has the given point.
     */
    static public Tile convertPointToTile(Point p,DungeonWorld level){
        int x = (int) p.x;
        int y = (int) p.y;
        return level.getTileAt(x, y);
    }
}