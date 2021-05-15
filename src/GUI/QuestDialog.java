package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;

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
    lbText = textStage.drawText("Can you find 10 watermelons for me?",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,200);

    lbReward = textStage.drawText("Rewards: 150 XP, Badass Sword",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,160);

    lbMenue = textStage.drawText("Y/N",
            "fonts/Pixeled.ttf", Color.YELLOW, 10,20,20,5,120);
  }
  //TODO: give show a parameter
  public void show(){
    this.isVisible= true;
    lbText.setText("Can you find 10 watermelons for me?");
    lbReward.setText("Rewards: 150 XP, Badass Sword");
    lbMenue.setText("Y/N");

  }
  //TODO: give Update a parameter
public void update(){
    show();
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
