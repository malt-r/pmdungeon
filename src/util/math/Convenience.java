package util.math;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class Convenience {

    /**
     * Normalize the difference-vector between two Points on a defined basis.
     *
     * @param p1                 The origin of the vector
     * @param p2                 The tip of the vector
     * @param normalizationBasis The basis on which the length of the difference-vector should be normalized.
     * @return A Point, of which the x and y members represent the components of the normalized vector.
     */
    static public Vec normalizeDelta(Vec p1, Vec p2, float normalizationBasis) {
        Vec diff = p2.subtract(p1);
        return diff.normalize(normalizationBasis);
    }

    static public Vec normalizeDelta(Point p1, Point p2, float normalizationBasis) {
        return normalizeDelta(new Vec(p1), new Vec(p2), normalizationBasis);
    }
}
