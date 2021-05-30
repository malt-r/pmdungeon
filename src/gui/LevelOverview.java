package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import main.Game;
import progress.Level;

public class LevelOverview implements LevelObserver {
  private final TextStage texthud;
  private final Game game;

  private Label levelLabel;

  /**
   * Leveloverview
   * @param texthud texthud
   */
  public LevelOverview(TextStage texthud) {
    this.texthud = texthud;
    this.game = Game.getInstance();

    levelLabelInit();

    game.getHero().getLevel().register(this);
  }

  private void levelLabelInit() {
    levelLabel =
        texthud.drawText(
            game.getHero().getLevel().getCurrentLevel()
                + "    "
                + game.getHero().getLevel().getCurrentXP()
                + "/"
                + game.getHero().getLevel().getXPForNextLevelTotal(),
            "fonts/Pixeled.ttf",
            Color.YELLOW,
            20,
            20,
            20,
            5,
            400);
  }

  /**
   * Function to update level display Gets called by Level.notifyObservers()
   *
   * @param level level object from which level the function got called
   */
  @Override
  public void update(Level level) {
    levelLabel.setText(
        level.getCurrentLevel()
            + " "
            + level.getCurrentXP()
            + "/"
            + level.getXPForNextLevelTotal());
  }
}
