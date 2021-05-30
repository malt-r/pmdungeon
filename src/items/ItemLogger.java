package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.scrolls.SupervisionScroll;
import items.shields.Shield;
import items.weapons.Weapon;
import stats.Modifier;

import java.util.logging.Logger;

/** Itemlogger class for logging information about items */
public class ItemLogger implements ItemVisitor {
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * visits a generic weapon
   *
   * @param weapon weapon which should be visited
   */
  @Override
  public void visit(Weapon weapon) {
    mainLogger.info("A weapon");
    printModifiers(weapon);
  }

  private void printModifiers(EquipableItem item) {
    for (var mod : item.modifiers) {
      printModifier(mod);
    }
  }

  private void printModifier(Modifier mod) {
    mainLogger.info(
        "affected stat: "
            + mod.getTypeOfEffectedAttribute().name()
            + mod.getType().name()
            + " val: "
            + mod.getValue());
  }

  /**
   * Visits a generic shield
   *
   * @param shield weapon which should be visited
   */
  @Override
  public void visit(Shield shield) {
    mainLogger.info("A shield");
    printModifiers(shield);
  }

  /**
   * visits a Poisonpotion
   *
   * @param potion weapon which should be visited
   */
  @Override
  public void visit(PoisonPotion potion) {
    mainLogger.info("Poison Potion, Damage:" + potion.getDamageValue());
  }

  /**
   * visits a heath potion
   *
   * @param potion weapon which should be visited
   */
  @Override
  public void visit(HealthPotion potion) {
    mainLogger.info("Healthy Potion, Healvalue: " + potion.getHealValue());
  }

  /**
   * visits an attack scroll
   *
   * @param scroll weapon which should be visited
   */
  @Override
  public void visit(AttackScroll scroll) {
    mainLogger.info("Attack Scroll, Bonus:" + scroll.getAttackBonus());
  }

  /**
   * visits a speedscroll
   *
   * @param scroll scroll which should be visited
   */
  @Override
  public void visit(SpeedScroll scroll) {
    mainLogger.info("SpeedScroll, Multiplier: " + scroll.getSpeedMultiplier());
  }

  /**
   * visits a supervision scroll
   *
   * @param scroll
   */
  @Override
  public void visit(SupervisionScroll scroll) {
    mainLogger.info("SupervisionScroll");
  }
}
