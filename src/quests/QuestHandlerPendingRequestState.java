package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * State in which user input is queried to handle a pending request for a new quest of the quest
 * handler.
 */
public class QuestHandlerPendingRequestState implements QuestHandlerState {

  private final QuestGiver giver;
  private final Quest pendingQuest;
  private boolean receivedInput;

  /**
   * constructor.
   *
   * @param pendingQuest the requested new quest.
   * @param giver the giver, which started the request for the new quest.
   */
  public QuestHandlerPendingRequestState(Quest pendingQuest, QuestGiver giver) {
    this.giver = giver;
    this.receivedInput = false;
    this.pendingQuest = pendingQuest;
  }

  /**
   * {@inheritDoc}
   *
   * @param handler the QuestHandler of the state machine
   */
  @Override
  public void enter(QuestHandler handler) {
    // call HUD
    mainLogger.info("Enter pending request");
    main.Game.getInstance().getQuestDialog().show(pendingQuest, handler.getCurrentQuest());
  }

  /**
   * {@inheritDoc}
   *
   * @param handler the QuestHandler of the state machine
   * @return the state of the quest
   */
  @Override
  public QuestHandlerState update(QuestHandler handler) {
    QuestHandlerState nextState = null;
    if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
      giver.questWasAccepted(true);
      this.receivedInput = true;
      handler.setupQuest(pendingQuest);
      nextState = handler.getIdleState();
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
      giver.questWasAccepted(false);
      this.receivedInput = true;
      nextState = handler.getIdleState();
    }
    return nextState;
  }

  /**
   * {@inheritDoc}
   *
   * @param handler the QuestHandler of the state machine
   */
  @Override
  public void exit(QuestHandler handler) {
    if (!this.receivedInput) {
      giver.questWasAccepted(false);
    }
    main.Game.getInstance().getQuestDialog().hide();
  }
}
