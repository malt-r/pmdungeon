package util.math;

import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

/** Simple mathematical 2D vector. */
public class Vec {
  private static final int X = 0;
  private static final int Y = 1;

  private final float[] val = new float[2];

  /**
   * Create a new Vec from a Point
   *
   * @param point The point to copy the values from.
   */
  public Vec(Point point) {
    val[X] = point.x;
    val[Y] = point.y;
  }

  /**
   * Create a new Vec from two components.
   *
   * @param x x
   * @param y y
   */
  public Vec(float x, float y) {
    val[X] = x;
    val[Y] = y;
  }

  /**
   * The x component.
   *
   * @return x
   */
  public float x() {
    return val[X];
  }

  /**
   * The y component.
   *
   * @return y
   */
  public float y() {
    return val[Y];
  }

  /**
   * Returns the added values of this Vec and the other Vec.
   *
   * @param other The Vec to add to this one.
   * @return A new Vec with added values of this Vec and the other Vec.
   */
  public Vec add(Vec other) {
    return new Vec(this.x() + other.x(), this.y() + other.y());
  }

  /**
   * Returns the subtracted values of this Vec and the other Vec.
   *
   * @param other The Vec to subtract form this one.
   * @return A new Vec with subtracted values of this Vec and the other Vec.
   */
  public Vec subtract(Vec other) {
    return new Vec(this.x() - other.x(), this.y() - other.y());
  }

  /**
   * Multiply this Vec with a scalar value.
   *
   * @param s The scalar value.
   * @return A new Vec which's components are multiplied with s.
   */
  public Vec multiply(float s) {
    return new Vec(this.x() * s, this.y() * s);
  }

  /**
   * Divide this Vec by a scalar value.
   *
   * @param s The scalar value, must not be zero.
   * @return A new Vec which's components are divided by s.
   * @throws ArithmeticException, if s is zero.
   */
  public Vec divide(float s) throws ArithmeticException {
    if (s == 0.0f) {
      throw new ArithmeticException("Divide by zero");
    }
    return new Vec(this.x() / s, this.y() / s);
  }

  /**
   * Scale this Vec to a given length.
   *
   * @param scale The scale.
   * @return
   */
  public Vec scale(float scale) {
    float magnitude = this.magnitude();

    if (magnitude != 0.0f) {
      var diffScaled = this.divide(magnitude).multiply(scale);
      return diffScaled;
    } else {
      return this;
    }
  }

  /**
   * Gets the magnitude of this Vec.
   *
   * @return The magnitude of this Vec.
   */
  public float magnitude() {
    return (float) Math.sqrt(Math.pow(this.x(), 2) + Math.pow(this.y(), 2));
  }

  /**
   * Creates a new Point from the values of this Vec.
   *
   * @return A new Point with the values of this Vec.
   */
  public Point toPoint() {
    return new Point(this.x(), this.y());
  }

  /**
   * Calculate the dot product of this and another Vec.
   *
   * @param other The other Vec.
   * @return The dot product.
   */
  public float dot(Vec other) {
    return this.x() * other.x() + this.y() * other.y();
  }
}
