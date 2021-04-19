import de.fhbielefeld.pmdungeon.vorgaben.game.Controller.MainController;

/**
 * The main game class.
 * <p>
 *     This class implements the MainController, which handles high level game logic, like the
 *     setup method and calling of the game loop.
 * </p>
 */
public class Game extends MainController {

    private Hero hero;

    /**
     * Setup of the game world.
     * <p>
     *     This will create the playable instance of the Hero.
     * </p>
     */
    @Override
    protected void setup() {
        hero = new Hero();

        // the entityController will call hero.update each frame
        entityController.addEntity(hero);

        // attach camera to hero
        camera.follow(hero);
    }

    /**
     * Implements logic executed at the begin of a frame.
     */
    @Override
    protected void beginFrame() {

    }

    /**
     * Implements logic executed at the end of a frame.
     * <p>
     *     This will check, if the Hero finished the current level and needs to be spawned in a new one.
     * </p>
     */
    @Override
    protected void endFrame() {
        // check, if current position of hero is on the trigger to load a new level
        if (levelController.checkForTrigger(hero.getPosition())) {
            levelController.triggerNextStage();
        }
    }

    /**
     * Implements logic executed on load of a new level.
     */
    @Override
    public void  onLevelLoad() {
        // set the level of the hero
        hero.setLevel(levelController.getDungeon());
    }
}
