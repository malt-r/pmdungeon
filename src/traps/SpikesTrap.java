package traps;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import main.ICombatable;
import java.util.Timer;
import java.util.TimerTask;
/**
 * The base class for any SpikesTrap.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld.
 * </p>
 */
public class SpikesTrap extends Trap{
  private float damageValue =10;

  /**
   * The SpikeTrap does damage on every "pointy" state change.
   * @return damageValue of the spikestrap
   */
  public float getDamageValue() {
    return damageValue;
  }

  private boolean nextFrame = true;
  private boolean animationRunsForward = true;
  private SpikesTrapState spikesTrapState = SpikesTrapState.NO_SPIKES;
  private final Timer nextFrameTimer;
  private Animation noSpikesAnimation;
  private Animation littleSpikesAnimation;
  private Animation middleSpikesAnimation;
  private Animation bigSpikesAnimation;
  /**
   * Constructor of the SpikesTrap class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public SpikesTrap(){
    super();
    nextFrameTimer = new Timer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateAnimations() {
    String[] noSpikes = new String[]{
            "tileset/traps/spikes/floor_spikes_anim_f0.png",
    };
    noSpikesAnimation = createAnimation(noSpikes,Integer.MAX_VALUE);

    String[] littleSpikes = new String[]{
            "tileset/traps/spikes/floor_spikes_anim_f1.png",
    };
    littleSpikesAnimation = createAnimation(littleSpikes,Integer.MAX_VALUE);

    String[] middleSpikes = new String[]{
            "tileset/traps/spikes/floor_spikes_anim_f2.png",
    };
    middleSpikesAnimation = createAnimation(middleSpikes,Integer.MAX_VALUE);

    String[] maxSpikes = new String[]{
            "tileset/traps/spikes/floor_spikes_anim_f3.png"
    };
    bigSpikesAnimation = createAnimation(maxSpikes, Integer.MAX_VALUE);

    currentAnimation= bigSpikesAnimation;
  }
  /**
   * {@inheritDoc}
   * Manages the state change between the spikes.
   */
  @Override
  public void update() {
    if(game.getDrawTraps()) {
      this.draw(-1, -1);
    }

    if(nextFrame){
      nextFrame= false;
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        nextFrame = true;
      }
    };
    nextFrameTimer.schedule(timerTask,1000);

    switch (spikesTrapState) {
      case NO_SPIKES:
        animationRunsForward= true;
        spikesTrapState = SpikesTrapState.LITTLE_SPIKES;
        currentAnimation = littleSpikesAnimation;
        break;
      case LITTLE_SPIKES:
        if(animationRunsForward){
          spikesTrapState = SpikesTrapState.MIDDLE_SPIKES;
          currentAnimation = middleSpikesAnimation;
          dealDamage();
        }else{
          spikesTrapState=SpikesTrapState.NO_SPIKES;
          currentAnimation=noSpikesAnimation;
        }
        break;
      case MIDDLE_SPIKES:
        //Do Damage
        if(animationRunsForward){
          spikesTrapState = SpikesTrapState.BIG_SPIKES;
          currentAnimation = bigSpikesAnimation;
          dealDamage();
        }else{
          spikesTrapState = SpikesTrapState.LITTLE_SPIKES;
          currentAnimation = littleSpikesAnimation;
        }
        break;
      case BIG_SPIKES:
        //Do Damage
        spikesTrapState = SpikesTrapState.MIDDLE_SPIKES;
        currentAnimation= middleSpikesAnimation;
        animationRunsForward = !animationRunsForward;
        break;
    }

    }
  }

  private void dealDamage(){
    if(game.checkForTrigger(this.position)){
        // TODO: why is only the hero affected by this trap?!
      ICombatable hero = game.getHero();
      if(spikesTrapState==SpikesTrapState.MIDDLE_SPIKES || spikesTrapState == SpikesTrapState.BIG_SPIKES){
        hero.dealDamage(getDamageValue(),null);
      }
    }
  }
}
