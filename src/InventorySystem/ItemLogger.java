package InventorySystem;

import java.util.logging.Logger;

public class ItemLogger implements IItemVisitor {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void visit(Spear1 spear) {
        mainLogger.info("Spear1, it's pointy");
    }

    @Override
    public void visit(Sword1 sword) {
        mainLogger.info("Sword1, dayum boi, he thicc");
    }

    @Override
    public void visit(SpecificPotion1 potion) {
        mainLogger.info("Potion1, deffo not poison, i promise");
    }

    @Override
    public void visit(SpecificPotion2 potion) {
        mainLogger.info("Potion2");
    }
}
