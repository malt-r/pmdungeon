package GUI;

import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import items.Item;
import items.inventory.Inventory;
import items.inventory.InventoryOpenState;
import items.inventory.OtherInventoryOpenState;
import items.inventory.OwnInventoryOpenState;
import main.Game;
import main.Hero;

public class InventoryOverview implements InventoryObserver, OpenStateObserver, HeroObserver{
    private HUD hud;
    private Game game;

    private InventoryIcon[] inventory = new InventoryIcon[10];
    private InventoryIcon[] chest = new InventoryIcon[10];
    private InventoryIcon[] heroSlots = new InventoryIcon[2];
    private InvBackgroundIcon[] invBackground = new InvBackgroundIcon[10];
    private InvBackgroundIcon[] chestBackground = new InvBackgroundIcon[10];

    public InventoryOverview(HUD hud){
        this.hud = hud;
        this.game = game.getInstance();
        invBackgroundInit();
        chestBackgroundInit();
        inventoryInit();
        chestInit();
        heroSlotsInit();

        game.getHero().getInventory().register(this);
        game.getHero().register(this);
    }

    private void invBackgroundInit(){
        for (int i = 0; i < 10; i++) {
            invBackground[i] = new InvBackgroundIcon(i, 0.0f);
            invBackground[i].setDefaultBackgroundTexture();
            hud.addHudElement(invBackground[i]);
        }
    }

    private void chestBackgroundInit() {
        for (int i = 0; i < 10; i++) {
            chestBackground[i] = new InvBackgroundIcon(i, 1.0f);
            hud.addHudElement(chestBackground[i]);
        }
    }

    private void inventoryInit() {
        for (int i = 0; i < 10; i++) {
            inventory[i] = new InventoryIcon(i, 0.0f);
            hud.addHudElement(inventory[i]);
        }
    }

    private void chestInit(){
        for (int i = 0; i < 10; i++) {
            chest[i] = new InventoryIcon(i, 1.0f);
            hud.addHudElement(chest[i]);
        }
    }

    private void heroSlotsInit(){
        for (int i = 0; i < 2; i++){
            heroSlots[i] = new InventoryIcon( i + 11, 0.0f);
            hud.addHudElement(heroSlots[i]);
        }
    }

    /**
     * Function to update inventory display
     * Gets called by Inventory.notifyObservers()
     * @param inv inv object from which inventory the function got called
     * @param fromHero boolean if its inventory of hero
     */
    @Override
    public void update(Inventory inv, boolean fromHero) {
        if (fromHero){
            updateHeroInv(inv);
        }else{
            updateChestInv(inv);
        }
    }

    private void updateChestInv(Inventory inv) {
        if (inv.getCurrentState() instanceof OtherInventoryOpenState) {
            for (int i = 0; i < chest.length; i++){
                chestBackground[i].setDefaultBackgroundTexture();
            }
            ((OtherInventoryOpenState) inv.getCurrentState()).register(this);
            chestBackground[((OtherInventoryOpenState) inv.getCurrentState()).getselectorIdx()].setPointerTexture();

            for (int i = 0; i < chest.length; i++){
                if (i < inv.getCount()){
                    chest[i].setTexture(inv.getItemAt(i).getTexture());
                } else {
                    chest[i].setDefaultTexture();
                }
            }
        }else {
            for (int i = 0; i < chest.length; i++) {
                chest[i].setDefaultTexture();
                chestBackground[i].setDefaultTexture();
            }
        }
    }

    private void updateHeroInv(Inventory inv) {
        for(int i = 0; i < invBackground.length; i++){
            invBackground[i].setDefaultBackgroundTexture();
        }

        if (inv.getCurrentState() instanceof OwnInventoryOpenState) {
            ((OwnInventoryOpenState) inv.getCurrentState()).register(this);
            int index = ((OwnInventoryOpenState) inv.getCurrentState()).getselectorIdx();
            if (index >= 0) {
                invBackground[index].setPointerTexture();
            }
        }

        for (int i = 0; i < inventory.length; i++){
            if (i < inv.getCount()){
                inventory[i].setTexture(inv.getItemAt(i).getTexture());
            } else {
                inventory[i].setDefaultTexture();
            }
        }
    }

    /**
     * Function to update index of inventories
     * Gets called by InventoryOpenState.notifyObservers()
     * @param invOp InventoryOpenState object from which the function is called
     */
    @Override
    public void update(InventoryOpenState invOp) {
        if (invOp instanceof OwnInventoryOpenState){
            for(int i = 0; i < invBackground.length; i++){
                invBackground[i].setDefaultBackgroundTexture();
            }
            invBackground[invOp.getselectorIdx()].setPointerTexture();
        }else if (invOp instanceof OtherInventoryOpenState){
            for(int i = 0; i < invBackground.length; i++){
                chestBackground[i].setDefaultBackgroundTexture();
            }
            chestBackground[invOp.getselectorIdx()].setPointerTexture();
        }
    }

    /**
     * Function to update hand slot display and calculate heart display
     * Gets called by Hero.notifyObservers()
     * @param hero hero object from which hero the function got called
     */
    @Override
    public void update(Hero hero) {

        Item leftHand = hero.getLeftHandSlot();
        Item rightHand = hero.getRightHandSlot();
        if (leftHand != null){
            heroSlots[0].setTexture(leftHand.getTexture());
        }else{
            heroSlots[0].setDefaultTexture();
        }

        if (rightHand != null){
            heroSlots[1].setTexture(rightHand.getTexture());
        }else{
            heroSlots[1].setDefaultTexture();
        }
    }
}
