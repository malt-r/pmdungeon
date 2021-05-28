package util.math;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

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

    static public int getRandBetween(int min, int max) {
        return (int)((Math.random() * (max - min)) + min);
    }

    static public Point getFlooredPoint(Point toFloor) {
        return new Point((float)Math.floor(toFloor.x), (float)Math.floor(toFloor.y));
    }

    static public boolean areFlooredPointsEqual(Point p1, Point p2) {
        if (Math.floor(p1.x) == Math.floor(p2.x) &&
                Math.floor(p1.y) == Math.floor(p2.y)) {
            return true;
        }
        return false;
    }
}
