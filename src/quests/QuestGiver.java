package quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private boolean isActivated;
    private QuestHandler questHandler;
    private Quest randomQuest;

    public QuestGiver(){
        //Safe Questhandler?
        this.game = Game.getInstance();
        questHandler = game.getQuestHandler();

        //TODO - Gen random quest
        randomQuest = new KillMonstersQuest(game.getHero());

        String[] idleLeftFrames = new String[]{
                "tileset/default/default_anim.png",
        };
        currentAnimation = createAnimation(idleLeftFrames, 6);
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
        this.draw();
        if (!isActivated & !waitingForInput){
            var allEntities = game.getAllEntities();
            for(var entity : allEntities){
                if (waitingForInput) { break; }
                if (!(entity instanceof Hero)) { continue; }
                var hero = (Hero) entity;
                if (!game.checkForIntersection(this, hero, level)){ continue; }
                System.out.println(randomQuest.getQuestName());
                System.out.println("\n y/n");
                waitingForInput = true;
            }
        }

        if (waitingForInput){
            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                questHandler.requestForNewQuest(randomQuest);
                waitingForInput = false;
                isActivated = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
                waitingForInput = false;
            }
        }
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
}
