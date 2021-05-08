package traps;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import main.ICombatable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpikesTrap extends Trap{
  private float damageValue =50;
  private ArrayList<ICombatable> hitInCurrentState;

  public float getDamageValue() {
    return damageValue;
  }

  private boolean nextFrame = true;
  private boolean animationRunsForward = true;
  private SpikesTrapState spikesTrapState = SpikesTrapState.NO_SPIKES;
  private final Timer nextFrameTimer;
  private final Animation noSpikesAnimation;
  private final Animation littleSpikesAnimation;
  private final Animation middleSpikesAnimation;
  private final Animation bigSpikesAnimation;
  public SpikesTrap(){
    super();
    hitInCurrentState = new ArrayList<>();
    nextFrameTimer = new Timer();
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
   * Called each frame, handles movement and the switching to and back from the running animation state.
   */
  @Override
  public void update() {
    if(isVisible) {
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
      ICombatable hero = (ICombatable) game.getAllEntities().get(0);
      if(spikesTrapState==SpikesTrapState.MIDDLE_SPIKES || spikesTrapState == SpikesTrapState.BIG_SPIKES){
        hero.dealDamage(10,null);
      }
    }
  }


}
