package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;

public class QuestOverview {
  private HUD hud;
  private TextStage textStage;
  private Label lbName;
  private Label lbProgress;
  public QuestOverview(HUD hud, TextStage textStage){
    this.hud= hud;
    this.textStage= textStage;
    lbName = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 20,20,20,300,400);

    lbProgress = textStage.drawText("",
            "fonts/Pixeled.ttf", Color.YELLOW, 20,20,20,300,360);
  }
  //TODO: give show a parameter
  public void show(){
    lbName.setText("Juicy watermelons");
    lbProgress.setText("1/10 Watermelons");
  }
  //TODO: give Update a parameter
  public void update(){
    show();
  }

  public void hide(){
    lbName.setText("");
    lbProgress.setText("");
  }

}
