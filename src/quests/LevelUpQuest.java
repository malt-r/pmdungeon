package quests;

import GUI.HeroObserver;
import main.Hero;

public class LevelUpQuest extends Quest implements HeroObserver {
    private int startLevel;
    private boolean isFinished;
    private Hero hero;

    @Override
    public String getQuestName() {
        return "Level go brrr";
    }

    @Override
    public String getProgressString() {
        return "Level up one level";
    }

    @Override
    public String getDescription() {
        return "Level up one level";
    }

    @Override
    public void setup() {
        this.hero.register(this);
        this.startLevel = this.hero.getLevel().getCurrentLevel();
        this.isFinished = false;
    }

    @Override
    public void cleanup() {
        this.hero.unregister(this);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void update(Hero hero) {
        if (hero.getLevel().getCurrentLevel() > this.startLevel) {
            this.isFinished = true;
        }
    }
}
