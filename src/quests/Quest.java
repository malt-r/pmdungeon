package quests;

import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class Quest implements IObservableQuest {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ArrayList<IQuestObserver> observers;
    protected QuestReward reward;

    public Quest() {
        observers = new ArrayList<>();
    }

    public abstract String getQuestName();

    public abstract String getProgressString();

    public abstract String getDescription();

    public String getRewardDescription() {
        return this.reward.getRewardDescription();
    }

    public abstract void setup();

    public abstract void cleanup();

    public abstract boolean isFinished();

    public void notifyObservers() {
        for (IQuestObserver observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void register(IQuestObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregister(IQuestObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    public QuestReward getReward(){
        return reward;
    }
}
