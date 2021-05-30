package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import main.Game;
import quests.QuestObserver;
import quests.Quest;

/**
 * QuestOvervoew class.
 *
 * <p>Defines the GUI Elements for a quest overview.
 */
public class QuestOverview implements QuestObserver {
  private final HUD hud;
  private final TextStage textStage;
  private final Label lbName;
  private final Label lbProgress;

  /**
   * Constructor of the QuestOverview class.
   *
   * <p>This constructor will instantiate the text instances of the HUD.
   */
  public QuestOverview(HUD hud, TextStage textStage) {
    this.hud = hud;
    this.textStage = textStage;
    lbName = textStage.drawText("", "fonts/Pixeled.ttf", Color.YELLOW, 10, 20, 20, 340, 400);

    lbProgress = textStage.drawText("", "fonts/Pixeled.ttf", Color.YELLOW, 10, 20, 20, 340, 360);
    Game.getInstance().getQuestHandler().register(this);
  }

  /**
   * Displays information of the current quest with progress.
   *
   * @param quest which should be displayed
   */
  public void show(Quest quest) {
    if (quest.isFinished()) {
      showFinishedQuest(quest);
    } else {
      showQuest(quest);
    }
  }

  /**
   * Displays a quest which has not be finished.
   *
   * @param quest which should be displayed
   */
  public void showQuest(Quest quest) {
    lbName.setColor(Color.YELLOW);
    lbName.setText(quest.getQuestName());
    lbProgress.setColor(Color.YELLOW);
    lbProgress.setText(quest.getProgressString());
  }

  /**
   * Display a finished quest.
   *
   * @param quest which should be displayed
   */
  public void showFinishedQuest(Quest quest) {
    lbName.setColor(Color.GREEN);
    lbName.setText(quest.getQuestName());
    lbProgress.setColor(Color.GREEN);
    lbProgress.setText("Abgeschlossen!");
  }

  /** Hides the questoverview. */
  public void hide() {
    lbName.setText("");
    lbProgress.setText("");
  }

  /**
   * Updates the quest overview.
   *
   * @param quest the quest, which sends the update
   */
  @Override
  public void update(Quest quest) {
    show(quest);
  }
}
