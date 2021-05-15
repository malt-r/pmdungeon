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
import java.util.logging.Logger;

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

    public QuestGiver(){
        //Safe Questhandler?
        this.game = Game.getInstance();
        questHandler = game.getQuestHandler();

        //TODO - Gen random quest
        quest = new KillMonstersQuest(game.getHero());

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

    @Override
    public Animation getActiveAnimation() {
        return currentAnimation;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void update() {
        this.draw(-1.0F, -0.7F);

        if (!questWasAccepted) {
            if (isHeroOnTile()){
                if (!waitingForInput & !wasOnTile) {
                    System.out.println("jao ich werde nicht gespammt!");
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

    @Override
    public boolean deleteable() {
        return false;
    }

    public void setLevel(DungeonWorld level) {
        this.level = level;
        findRandomPosition();
    }

    public void findRandomPosition() {
        this.position = new Point(level.getRandomPointInDungeon());
    }

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
