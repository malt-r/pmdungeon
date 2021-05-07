package progress;

public class Level {
    private int level;
    private int xp;
    private int xpConstant = 10;

    public Level() {
        this.level = 1;
    }

    public int getCurrentXP() {
        return xp;
    }

    // TODO: provide observable pattern for level up
    public boolean increaseXP(int amount) {
        this.xp += amount;
        // level up
        int xpForNextLevel = getXPForNextLevel();
        if (this.xp > xpForNextLevel) {
            this.xp -= xpForNextLevel;
            this.level += 1;
            return true;
        } else {
            return false;
        }
    }

    public int getXPForNextLevel() {
        // level = sqrt(XP) / constant
        // XP = (level*constant) ^2
        return (int)Math.pow(((double)this.level * (double)this.xpConstant), 2);
    }

    public int getCurrentLevel() {
        return level;
    }
}
