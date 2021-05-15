package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import quests.Quest;

public class QuestDialog {
  private HUD hud;
  private TextStage textStage;
  private Label lbText;
  private Label lbReward;
  private Label lbMenue;
  private boolean isVisible=false;

  public QuestDialog(HUD hud, TextStage textStage){
    this.hud= hud;
    this.textStage= textStage;
    lbText = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,200);

    lbReward = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,160);

    lbMenue = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,120);

  }
  //TODO: give show a parameter
  public void show(Quest newQuest, Quest currentQuest){
      if (null != newQuest) {
          this.isVisible = true;
          lbText.setText("Quest '" + newQuest.getQuestName() + "' akzeptieren? (" + newQuest.getDescription() + ")");
          lbReward.setText(newQuest.getRewardDescription());
          if (null != currentQuest && !currentQuest.isFinished()) {
              lbMenue.setText("J/N (Akt. Quest '" + currentQuest.getQuestName() + "' abbrechen)");
          } else {
              lbMenue.setText("J/N");
          }
      }
  }

  public void hide(){
    this.isVisible= false;
    lbText.setText("");
    lbReward.setText("");
    lbMenue.setText("");
  }

  public boolean getIsVisible(){
    return this.isVisible;
  }
}
