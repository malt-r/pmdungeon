package main;

/**
 * 'Stores' an amount of one item type.
 * @param <T> The type of the item.
 */
public class ItemStack<T extends Item> {
    private int count;
    private int capacity;
    private T item;

    public ItemStack(T item, int count) {
        // init count
        this.count = 0;

        // init capacity
        this.item = item;
        if (this.item.isStackable()) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            this.capacity = 1;
        }

        // push count
        this.push(count);
    }

    /**
     * Returns the effective count of pushed items.
     * @param count
     * @return
     */
    public int push(int count) {
        int newCount = this.count + count;
        if (newCount > this.capacity) {
            int diff = newCount - this.count;
            this.count = this.capacity;
            return diff;
        } else  {
            this.count += count;
            return count;
        }
    }

    public int getCount() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count <= 0;
    }

    /**
     * Returns the effective count of popped items.
     * @param count
     * @return
     */
    public int pop(int count) {
        int poppedAmount = 0;
        if (count > this.count) {
            poppedAmount = this.count;
        } else {
            poppedAmount = count;
            this.count -= count;
        }
        return poppedAmount;
    }


}
