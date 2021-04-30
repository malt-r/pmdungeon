package main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Inventory {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private int capacity;

    // needs to be synchronized with the ArrayList at all times..
    private HashMap<Type, Integer> itemMap;
    private ArrayList<ItemStack> items;

    public Inventory(int capacity) {
        itemMap = new HashMap<Type, Integer>();
        items = new ArrayList<ItemStack>();
    }

    public <T extends Item> boolean AddItem(T item, int count) {
        var itemType = item.getClass();

        if (!items.containsKey(item.getClass())) { // add new stack
            if (this.items.size() < this.capacity) {
                items.put(itemType, new ItemStack(item, count));
            } else {
                mainLogger.info("Capacity if inventory already reached");
                return false;
            }
        } else { // increase size of existing stack by count
            items.get(itemType).push(count);
        }
        return true;
    }

    public void LogContent() {
        // TODO;
    }

    public Item getSelectedItem() {

    }
}
