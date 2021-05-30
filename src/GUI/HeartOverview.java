package GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import main.Game;
import main.Hero;

public class HeartOverview implements HeroObserver {
  private final HUD hud;
  private final TextStage textHUD;
  private final HeartIcon[] hearts = new HeartIcon[10];
  private final Game game;
  private Label heartLabel;

  public HeartOverview(HUD hud, TextStage textHUD) {
    this.hud = hud;
    this.textHUD = textHUD;
    this.game = Game.getInstance();

    heartsInit();
    heartLabelInit();

    heartCalc();

    game.getHero().register(this);
  }

  private void heartsInit() {
    for (int i = 0; i < 10; i++) {
      hearts[i] = new HeartIcon(i);
      hud.addHudElement(hearts[i]);
    }
  }

  private void heartLabelInit() {
    heartLabel = textHUD.drawText("", "fonts/Pixeled.ttf", Color.RED, 20, 20, 20, 50, 445);
  }

  @Override
  public void update(Hero hero) {
    heartCalc();
  }

  /** Calculates and sets the heart display */
  private void heartCalc() {
    float health = game.getHero().getHealth();
    int heartHalves = (int) Math.ceil(health / 10);
    int heartFull = heartHalves / 2;

    heartLabel.setText("");

    if (health <= 200.0f) {
      int i = 0;

      for (i = 0; i < heartFull; i++) {
        hearts[i].setState(2);
      }
      if (heartHalves % 2 == 1) {
        hearts[i].setState(1);
        i++;
      }
      for (int j = i; j < 10; j++) {
        hearts[j].setState(0);
      }
    } else {
      hearts[0].setState(2);
      for (int i = 1; i < hearts.length; i++) {
        hearts[i].setDefaultTexture();
      }

      heartLabel.setText("" + heartFull);
    }
  }
}
