package items;

import items.potions.HealthPotion;
import items.potions.PoisonPotion;
import items.scrolls.AttackScroll;
import items.scrolls.SpeedScroll;
import items.shields.Shield;
import items.weapons.Weapon;

import java.util.logging.Logger;

public class ItemLogger implements IItemVisitor {
  protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void visit(Weapon weapon) { mainLogger.info("Regular Sword, dayum boi, he thicc"); }

  @Override
  public void visit(Shield shield) { mainLogger.info("Jo das ist nen Schild");}

  @Override
  public void visit(PoisonPotion potion) { mainLogger.info(String.valueOf(potion.damageValue));}

  @Override
  public void visit(HealthPotion potion) {
    mainLogger.info("Healthy Potion, for bigger buh buhs");
  }

  @Override
  public void visit(AttackScroll scroll) { mainLogger.info(String.valueOf(scroll.healValue)); }

  @Override
  public void visit(SpeedScroll scroll) { mainLogger.info(String.valueOf(scroll.healValue)); }
}
