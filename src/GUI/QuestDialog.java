package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import quests.Quest;

/**
 * QuestDialog class.
 *
 * <p>Defines the GUI Elements for a quest dialog.
 */
public class QuestDialog {
  private final HUD hud;
  private final TextStage textStage;
  private final Label lbText;
  private final Label lbReward;
  private final Label lbMenue;
  private boolean isVisible = false;

  /**
   * Constructor of the QuestDialog class.
   *
   * <p>This constructor will instantiate the text instances of the HUD.
   */
  public QuestDialog(HUD hud, TextStage textStage) {
    this.hud = hud;
    this.textStage = textStage;
    lbText = textStage.drawText("", "fonts/Pixeled.ttf", Color.YELLOW, 10, 20, 20, 5, 200);

    lbReward = textStage.drawText("", "fonts/Pixeled.ttf", Color.YELLOW, 10, 20, 20, 5, 160);

    lbMenue = textStage.drawText("", "fonts/Pixeled.ttf", Color.YELLOW, 10, 20, 20, 5, 120);
  }

  /**
   * display a questdialog
   *
   * @param newQuest quest which should be drawn
   * @param currentQuest which is drawn currently
   */
  public void show(Quest newQuest, Quest currentQuest) {
    if (null != newQuest) {
      this.isVisible = true;
      lbText.setText(
          "Quest '"
              + newQuest.getQuestName()
              + "' akzeptieren? ("
              + newQuest.getDescription()
              + ")");
      lbReward.setText(newQuest.getRewardDescription());
      if (null != currentQuest && !currentQuest.isFinished()) {
        lbMenue.setText("J/N (Akt. Quest '" + currentQuest.getQuestName() + "' abbrechen)");
      } else {
        lbMenue.setText("J/N");
      }
    }
  }

  /** hides the questdialog */
  public void hide() {
    this.isVisible = false;
    lbText.setText("");
    lbReward.setText("");
    lbMenue.setText("");
  }

  /**
   * gets the visibility state of the questdialog
   *
   * @return if the questdialog is visible
   */
  public boolean getIsVisible() {
    return this.isVisible;
  }
}
