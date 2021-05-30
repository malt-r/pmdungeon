package quests;

import main.DrawableEntity;
import main.Game;
import main.Hero;

import java.util.Random;

import static util.math.Convenience.checkForIntersection;

/** Implementation of a QuestFiver that gives an qust. */
public class QuestGiver extends DrawableEntity {
  private final QuestHandler questHandler;
  private boolean waitingForInput;
  private boolean questWasAccepted;
  private boolean wasOnTile;
  private Quest quest;

  /** constructor Initializes the questgiver with a random quest. */
  public QuestGiver() {
    this.questHandler = game.getQuestHandler();

    Random dice = new Random();
    var questIndex = dice.nextInt(3);
    switch (questIndex) {
      case 0:
        quest = new CollectItemsQuest(game.getHero());
        break;
      case 1:
        quest = new KillMonstersQuest(game.getHero());
        break;
      case 2:
        quest = new LevelUpQuest(game.getHero());
        break;
      default:
        assert (true);
        break;
    }
    mainLogger.info("Spawned with " + quest);
  }

  /** {@inheritDoc} */
  @Override
  protected void generateAnimations() {
    String[] idleLeftFrames =
        new String[] {
          "tileset/other/quests/pumpkin_dude_anim1.png",
          "tileset/other/quests/pumpkin_dude_anim2.png",
          "tileset/other/quests/pumpkin_dude_anim3.png",
          "tileset/other/quests/pumpkin_dude_anim4.png"
        };
    currentAnimation = createAnimation(idleLeftFrames, 4);
  }

  /**
   * Draws the questgiver with right scaling and checks if the hero is on the same tile as well as
   * checking if the quest was accepted.
   */
  @Override
  public void update() {
    this.draw(-1.0F, -0.7F);

    if (!questWasAccepted) {
      if (isHeroOnTile()) {
        if (!waitingForInput & !wasOnTile) {
          questHandler.requestForNewQuest(this.quest, this);
          waitingForInput = true;
        }
        wasOnTile = true;
      } else {
        // Abort quest request
        wasOnTile = false;
        if (waitingForInput) {
          abortPendingQuestRequest();
        }
      }
    }
  }

  private void abortPendingQuestRequest() {
    Game.getInstance().getQuestHandler().abortNewQuestRequest();
    // Oder Questhandler.abortNewQuestRequest();
  }

  /**
   * Disables the waiting procedure if the quest has been accepted.
   *
   * @param status
   */
  public void questWasAccepted(boolean status) {
    if (!questWasAccepted) {
      questWasAccepted = status;
      waitingForInput = false;
    }
  }

  private boolean isHeroOnTile() {
    var nearEntities = game.getEntitiesInNeighborFields(this.getPosition());
    for (var entity : nearEntities) {
      if (!(entity instanceof Hero)) {
        continue;
      }
      var hero = (Hero) entity;
      if (!checkForIntersection(this, hero)) {
        continue;
      }
      return true;
    }
    return false;
  }
}
