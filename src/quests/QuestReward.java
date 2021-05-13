package quests;

import items.Item;

import java.util.ArrayList;

public class QuestReward {
    private ArrayList<Item> items;
    private int xp;
    private String desc;

    public QuestReward(ArrayList<Item> items, int xp) {
        this.items = new ArrayList<>(items);
        this.xp = xp;

        this.desc = generateDesc();
    }

    private String generateDesc() {
        StringBuilder builder = new StringBuilder();
        builder.append("Reward: ");
        for (int i = 0; i < items.size(); i++) {
            builder.append(items.get(i).getName());
            if (i < items.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(xp);
        builder.append(" XP");
        return builder.toString();
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public int getXp() {
        return this.xp;
    }

    public String getRewardDescription() {
        return desc;
    }
}
