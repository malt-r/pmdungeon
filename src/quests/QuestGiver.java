package quests;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;
import main.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Implementation of a QuestFiver that gives an qust.
 */
public class QuestGiver implements IAnimatable, IEntity {
    private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Animation currentAnimation;
    private Game game;
    private Point position;
    private DungeonWorld level;
    private boolean waitingForInput;
    private boolean questWasAccepted;
    private boolean wasOnTile;
    private QuestHandler questHandler;
    private Quest quest;

    /**
     * constructor
     * Initializes the questgiver with a random quest.
     */
    public QuestGiver(){

        this.game = Game.getInstance();
        this.questHandler = game.getQuestHandler();

        Random dice = new Random();
        var questIndex = dice.nextInt(3);
        switch (questIndex){
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
                assert(true);
                break;
        }
        mainLogger.info("Spawned with " + quest.toString());

        String[] idleLeftFrames = new String[]{
                "tileset/other/quests/pumpkin_dude_anim1.png",
                "tileset/other/quests/pumpkin_dude_anim2.png",
                "tileset/other/quests/pumpkin_dude_anim3.png",
                "tileset/other/quests/pumpkin_dude_anim4.png"
        };
        currentAnimation = createAnimation(idleLeftFrames, 4);
    }

    private Animation createAnimation(String[] texturePaths, int frameTime) {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {
            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Animation getActiveAnimation() {
        return currentAnimation;
    }
    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Point getPosition() {
        return position;
    }

    /**
     * Draws the questgiver with right scaling and checks if the hero is on the same tile as well as
     * checking if the quest was accepted.
     */
    @Override
    public void update() {
        this.draw(-1.0F, -0.7F);

        if (!questWasAccepted) {
            if (isHeroOnTile()){
                if (!waitingForInput & !wasOnTile) {
                    questHandler.requestForNewQuest(this.quest, this);
                    waitingForInput = true;
                }
                wasOnTile = true;
            } else {
                //Abort quest request
                wasOnTile = false;
                if (waitingForInput) {
                    abortPendingQuestRequest();
                }
            }
        }
    }

    private void abortPendingQuestRequest() {
        Game.getInstance().getQuestHandler().abortNewQuestRequest();
        //Oder Questhandler.abortNewQuestRequest();
    }
    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean deleteable() {
        return false;
    }

    /**
     * sets the level of the questgiver
     * @param level in which the questgiver is in
     */
    public void setLevel(DungeonWorld level) {
        this.level = level;
        findRandomPosition();
    }

    /**
     * Sets the position of the questgiver to a random position in the dungeon.
     */
    public void findRandomPosition() {
        this.position = new Point(level.getRandomPointInDungeon());
    }

    /**
     * Disables the waiting procedure if the quest has been accepted.
     * @param status
     */
    public void questWasAccepted(boolean status) {
        if (!questWasAccepted) {
            questWasAccepted = status;
            waitingForInput = false;
        }
    }

    private boolean isHeroOnTile(){
        var allEntities = game.getAllEntities();
        for(var entity : allEntities) {
            if (!(entity instanceof Hero)) {
                continue;
            }
            var hero = (Hero) entity;
            if (!game.checkForIntersection(this, hero, level)) {
                continue;
            }
            return true;
        }
        return false;
    }
}
