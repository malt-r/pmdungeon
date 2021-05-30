package monsters;

import main.Game;
import monsters.strategies.combat.CombatStrategy;
import monsters.strategies.combat.MeleeStrategy;
import monsters.strategies.combat.RangedcombatStrategy;
import monsters.strategies.movement.FollowHeroStrategy;
import monsters.strategies.movement.MovementStrategy;
import monsters.strategies.movement.RandomMovementStrategy;
import util.math.Convenience;

/**
 * Demon Monster.
 * <p>
 *   Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class DemonMonster extends Monster{
  private final MovementStrategy randomMovementStrategy;
  private final MovementStrategy followHeroStrategy;
  private final CombatStrategy meleeCombatStrategy;
  private final CombatStrategy rangecombatStrategy;
  /**
   * Constructor of the Demonmonster class.
   * <p>
   *     This constructor will instantiate the animations and read all required texture data.
   * </p>
   */
  public DemonMonster(){
  super();
  randomMovementStrategy = new RandomMovementStrategy();
  followHeroStrategy = new FollowHeroStrategy();
  currentMovementStrategy = randomMovementStrategy;

  meleeCombatStrategy = new MeleeStrategy();
  rangecombatStrategy = new RangedcombatStrategy();
  }

  /**
   * Generates the run and idle animation for the Demon monster.
   */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames = new String[]{
            "tileset/demon/big_demon_idle_left_anim_f0.png",
            "tileset/demon/big_demon_idle_left_anim_f1.png",
            "tileset/demon/big_demon_idle_left_anim_f2.png",
            "tileset/demon/big_demon_idle_left_anim_f3.png"
    };
    idleAnimationLeft = createAnimation(idleLeftFrames, 6);

    String[] idleRightFrames = new String[]{
            "tileset/demon/big_demon_idle_anim_f0.png",
            "tileset/demon/big_demon_idle_anim_f1.png",
            "tileset/demon/big_demon_idle_anim_f2.png",
            "tileset/demon/big_demon_idle_anim_f3.png"
    };
    idleAnimationRight = createAnimation(idleRightFrames, 6);

    String[] runLeftFrames = new String[]{
            "tileset/demon/big_demon_run_left_anim_f0.png",
            "tileset/demon/big_demon_run_left_anim_f1.png",
            "tileset/demon/big_demon_run_left_anim_f2.png",
            "tileset/demon/big_demon_run_left_anim_f3.png"
    };
    runAnimationLeft = createAnimation(runLeftFrames, 4);

    String[] runRightFrames = new String[]{
            "tileset/demon/big_demon_run_anim_f0.png",
            "tileset/demon/big_demon_run_anim_f1.png",
            "tileset/demon/big_demon_run_anim_f2.png",
            "tileset/demon/big_demon_run_anim_f3.png"
    };
    runAnimationRight = createAnimation(runRightFrames, 4);

    String[] hitLeftFrames = new String[]{
            "tileset/demon/big_demon_idle_left_anim_f0_hit.png"
    };
    hitAnimationLeft = createAnimation(hitLeftFrames, 1);

    String[] hitRightFrames = new String[]{
            "tileset/demon/big_demon_idle_anim_f0_hit.png"
    };
    hitAnimationRight = createAnimation(hitRightFrames, 1);
  }

  /**
   * Draws the monster with correct scaling.
   */
  @Override
  public void draw(){
    drawWithScaling(2.0f,2.0f);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(){
    var startTile = Convenience.convertPointToTile(getPosition(),level);
    var endTile = Convenience.convertPointToTile(Game.getInstance().getHero().getPosition(),level);
    var pathToHero = level.findPath(startTile, endTile);
    if (pathToHero.getCount() > 2) {
      currentCombatStrategy = this.rangecombatStrategy;

    }else{
      currentCombatStrategy = this.meleeCombatStrategy;
    }
    if(pathToHero.getCount()<10){
      currentMovementStrategy = this.followHeroStrategy;
    }else{
      currentMovementStrategy = this.randomMovementStrategy;
    }
    super.update();
  }

  /**
   * gets the damage value for combat
   * @return damage value of the actor
   */
  //@Override
  //public float getDamage() {
  //  return this.currentCombatStrategy.getAttackValue() * this.attackDamageModifierWeapon * this.attackDamageModifierScroll;
  //}

}