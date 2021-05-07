package progress;

public class Level {
    private int level;
    private int xp;
    private float xpConstant = 5.20f; //
    private float healthScaleConstant = 3.f;
    private float damageScaleConstant = 2.f;

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
        int xpForNextLevel = getXPForNextLevelTotal();
        if (this.xp >= xpForNextLevel) {
            this.xp -= xpForNextLevel;
            this.level += 1;
            return true;
        } else {
            return false;
        }
    }

    public float getHealthIncrementForCurrentLevel() {
        // first test
        return (int)Math.pow(((double)this.level * (double)this.healthScaleConstant), 2);
    }

    public float getDamageIncrementForCurrentLevel() {
        // first test
        return (int)Math.pow(((double)this.level * (double)this.damageScaleConstant), 2);
    }

    public int getXPForNextLevelLeft() {
        int total = getXPForNextLevelTotal();
        return total - this.xp;
    }

    public int getXPForNextLevelTotal() {
        // level = sqrt(XP) / constant
        // XP = (level*constant) ^2
        // first test
        return (int)Math.pow(((double)this.level * (double)this.xpConstant), 2);
    }

    public int getCurrentLevel() {
        return level;
    }
}
