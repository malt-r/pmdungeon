package GUI;

import items.inventory.Inventory;

/** Interface for a inventory observer */
public interface InventoryObserver {
  /**
   * updates the observer and passes the inventory and a flag if the updated was created by the
   * hero.
   *
   * @param inv nventory which should be observed
   * @param fromHero wether the update comes from the hero
   */
  void update(Inventory inv, boolean fromHero);
}
