package GUI;

import main.Hero;

public interface HeroObserver extends Observer {
    public void update(Hero hero);
}
