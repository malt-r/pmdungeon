package quests;

import items.Item;

import java.util.ArrayList;

/**
 * Rewards of a quest. Encapsulates list of items and amount of xp.
 */
public class QuestReward {
    private ArrayList<Item> items;
    private int xp;
    private String desc;

    /**
     * constructor
     * @param items itemlist to encapsulate
     * @param xp amount of xp to encapsulate
     */
    public QuestReward(ArrayList<Item> items, int xp) {
        this.items = new ArrayList<>(items);
        this.xp = xp;

        this.desc = generateDesc();
    }

    // generates a description of the rewards
    private String generateDesc() {
        StringBuilder builder = new StringBuilder();
        builder.append("Reward: ");
        for (int i = 0; i < items.size(); i++) {
            builder.append(items.get(i).getName());
            builder.append(", ");
        }
        builder.append(xp);
        builder.append(" XP");
        return builder.toString();
    }

    /**
     * Getter for the encapsulated item-list
     * @return the encapsulated item list
     */
    public ArrayList<Item> getItems() {
        return this.items;
    }

    /**
     * Getter for the amount of encapsulated xp.
     * @return xp-amount.
     */
    public int getXp() {
        return this.xp;
    }

    /**
     * Getter for the reward description.
     * @return reward description.
     */
    public String getRewardDescription() {
        return desc;
    }
}
