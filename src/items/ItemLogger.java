package items;

import items.potions.HealthPotion;
import items.scrolls.AttackScroll;
import items.weapons.RegularSword;
import items.weapons.Weapon;

import java.util.logging.Logger;

public class ItemLogger implements IItemVisitor {
  protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void visit(Weapon weapon) { mainLogger.info("Regular Sword, dayum boi, he thicc");
  }

  @Override
  public void visit(HealthPotion potion) {
    mainLogger.info("Healthy Potion, for bigger buh buhs");
  }

  @Override
  public void visit(AttackScroll scroll) {
    mainLogger.info(String.valueOf(scroll.healValue));
  }
}
