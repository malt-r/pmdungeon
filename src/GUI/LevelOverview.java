package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import main.Game;
import progress.Level;

public class LevelOverview implements LevelObserver {
    private TextStage textHUD;
    private Game game;

    private Label levelLabel;

    public LevelOverview(TextStage textHUD){
        this.textHUD = textHUD;
        this.game = game.getInstance();

        levelLabelInit();

        game.getHero().getLevel().register(this);
    }

    private void levelLabelInit(){
        levelLabel = textHUD.drawText(game.getHero().getLevel().getCurrentLevel() + "    " +
                        game.getHero().getLevel().getCurrentXP() + "/" +
                        game.getHero().getLevel().getXPForNextLevelTotal(),
                "fonts/Pixeled.ttf", Color.YELLOW, 20,20,20,5,400);
    }

    /**
     * Function to update level display
     * Gets called by Level.notifyObservers()
     * @param level level object from which level the function got called
     */
    @Override
    public void update(Level level) {
        levelLabel.setText(level.getCurrentLevel() + " " + level.getCurrentXP() + "/" + level.getXPForNextLevelTotal());
    }
}
