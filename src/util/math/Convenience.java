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
}
