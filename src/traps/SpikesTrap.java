package traps;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import java.util.Timer;
import java.util.TimerTask;
import main.ICombatable;


/**
 * The base class for any SpikesTrap.
 *
 * <p>Contains all animations, the current position in the DungeonWorld.
 */
public class SpikesTrap extends Trap {
  private final float damageValue = 10;
  private final Timer nextFrameTimer;
  private boolean nextFrame = true;
  private boolean animationRunsForward = true;
  private SpikesTrapState spikesTrapState = SpikesTrapState.NO_SPIKES;
  private Animation noSpikesAnimation;
  private Animation littleSpikesAnimation;
  private Animation middleSpikesAnimation;
  private Animation bigSpikesAnimation;
  /**
   * Constructor of the SpikesTrap class.
   *
   * <p>This constructor will instantiate the animations and read all required texture data.
   */

  public SpikesTrap() {
    super();
    nextFrameTimer = new Timer();
    super.collisionCenterOffset.x = -0.2f;
    super.collisionCenterOffset.y = -0.3f;
    super.activationDistance = 0.7f;
  }

  /**
   * The SpikeTrap does damage on every "pointy" state change.
   *
   * @return damageValue of the spikestrap
   */
  public float getDamageValue() {
    return damageValue;
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] noSpikes =
        new String[] {
          "tileset/traps/spikes/floor_spikes_anim_f0.png",
        };
    noSpikesAnimation = createAnimation(noSpikes, Integer.MAX_VALUE);

    String[] littleSpikes =
        new String[] {
          "tileset/traps/spikes/floor_spikes_anim_f1.png",
        };
    littleSpikesAnimation = createAnimation(littleSpikes, Integer.MAX_VALUE);

    String[] middleSpikes =
        new String[] {
          "tileset/traps/spikes/floor_spikes_anim_f2.png",
        };
    middleSpikesAnimation = createAnimation(middleSpikes, Integer.MAX_VALUE);

    String[] maxSpikes = new String[] {"tileset/traps/spikes/floor_spikes_anim_f3.png"};
    bigSpikesAnimation = createAnimation(maxSpikes, Integer.MAX_VALUE);

    currentAnimation = bigSpikesAnimation;
  }

  /** {@inheritDoc} Manages the state change between the spikes. */
  @Override
  public void update() {
    if (game.getDrawTraps()) {
      this.draw(-1, -1);
    }

    if (nextFrame) {
      nextFrame = false;
      TimerTask timerTask =
          new TimerTask() {
            @Override
            public void run() {
              nextFrame = true;
            }
          };
      nextFrameTimer.schedule(timerTask, 100);

      switch (spikesTrapState) {
        case NO_SPIKES:
          animationRunsForward = true;
          spikesTrapState = SpikesTrapState.LITTLE_SPIKES;
          currentAnimation = littleSpikesAnimation;
          break;
        case LITTLE_SPIKES:
          if (animationRunsForward) {
            spikesTrapState = SpikesTrapState.MIDDLE_SPIKES;
            currentAnimation = middleSpikesAnimation;
            dealDamage();
          } else {
            spikesTrapState = SpikesTrapState.NO_SPIKES;
            currentAnimation = noSpikesAnimation;
          }
          break;
        case MIDDLE_SPIKES:
          // Do Damage
          if (animationRunsForward) {
            spikesTrapState = SpikesTrapState.BIG_SPIKES;
            currentAnimation = bigSpikesAnimation;
            dealDamage();
          } else {
            spikesTrapState = SpikesTrapState.LITTLE_SPIKES;
            currentAnimation = littleSpikesAnimation;
          }
          break;
        case BIG_SPIKES:
          // Do Damage
          spikesTrapState = SpikesTrapState.MIDDLE_SPIKES;
          currentAnimation = middleSpikesAnimation;
          animationRunsForward = !animationRunsForward;
          break;
        default:
          break;
      }
    }
  }

  private void dealDamage() {
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
    for (var entitiy : nearEntities) {
      if (entitiy instanceof ICombatable) {
        if (super.checkForIntersectionWithDrawable(entitiy)) {
          if (spikesTrapState == SpikesTrapState.MIDDLE_SPIKES
              || spikesTrapState == SpikesTrapState.BIG_SPIKES) {
            ((ICombatable) entitiy).dealDamage(getDamageValue(), null);
          }
        }
      }
    }
  }
}
