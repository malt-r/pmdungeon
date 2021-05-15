package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import main.Game;
import quests.IQuestObserver;
import quests.Quest;

public class QuestOverview implements IQuestObserver {
  private HUD hud;
  private TextStage textStage;
  private Label lbName;
  private Label lbProgress;

  public QuestOverview(HUD hud, TextStage textStage){
    this.hud= hud;
    this.textStage= textStage;
    lbName = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,340,400);

    lbProgress = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,340,360);
    Game.getInstance().getQuestHandler().register(this);
  }

  public void show(Quest quest) {
      if (quest.isFinished()) {
          showFinishedQuest(quest);
      } else {
          showQuest(quest);
      }
  }

  public void showQuest(Quest quest) {
      lbName.setColor(Color.YELLOW);
      lbName.setText(quest.getQuestName());
      lbProgress.setColor(Color.YELLOW);
      lbProgress.setText(quest.getProgressString());
  }

  public void showFinishedQuest(Quest quest) {
      lbName.setColor(Color.GREEN);
      lbName.setText(quest.getQuestName());
      lbProgress.setColor(Color.GREEN);
      lbProgress.setText("Abgeschlossen!");
  }

  public void hide(){
    lbName.setText("");
    lbProgress.setText("");
  }

  @Override
  public void update(Quest quest) {
    show(quest);
  }
}
