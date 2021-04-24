package util.math;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

public class Vec {
    private static final int X = 0;
    private static final int Y = 1;

    private float[] val = new float[2];

    public float x() {
        return val[X];
    }
    public float y() {
        return val[Y];
    }

    public Vec(Point point) {
        val[X] = point.x;
        val[Y] = point.y;
    }

    public Vec(float x, float y) {
        val[X] = x;
        val[Y] = y;
    }

    public Vec add(Vec other) {
        return new Vec(this.x() + other.x(), this.y() + other.y());
    }

    public Vec subtract(Vec other) {
        return new Vec(this.x() - other.x(), this.y() - other.y());
    }

    public Vec multiply(float s) {
        return new Vec(this.x() * s, this.y() * s);
    }

    public Vec divide(float s) throws ArithmeticException {
        if (s == 0.0f) {
            throw new ArithmeticException("Divide by zero");
        }
        return new Vec(this.x() / s, this.y() / s);
    }

    public Vec normalize(float normalizationBasis) {
        float magnitude = this.magnitude();

        if (magnitude != 0.0f) {
            var diffNormalized = this.divide(magnitude).multiply(normalizationBasis);
            return diffNormalized;
        } else {
            return this;
        }
    }

    public float magnitude() {
        return (float)Math.sqrt(Math.pow(this.x(), 2) + Math.pow(this.y(), 2));
    }

    public Point toPoint() {
        return new Point(this.x(), this.y());
    }

    public float dot(Vec other) {
        return this.x() * other.x() + this.y() * other.y();
    }
}