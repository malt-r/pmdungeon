package items.scrolls;

import items.UseableItem;

/**
 * Scroll.
 *
 * <p>Contains everything that describes a scroll.
 */
public abstract class Scroll extends UseableItem {
  /**
   * Constructor of the abstract Scroll class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */
  public Scroll() {
    super();
  }

  /** Called each frame and draws the scroll. */
  @Override
  public void update() {
    draw(-0.85F, -0.75F, 0.75f, 0.75f);
  }
}
