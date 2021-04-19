import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The controllable player character.
 * <p>
 *     Contains all animations, the current position in the DungeonWorld and movement logic.
 * </p>
 */
public class Hero implements IAnimatable, IEntity {
    private Point position;
    private DungeonWorld level;

    private final Animation idleAnimationRight;
    private final Animation idleAnimationLeft;
    private final Animation runAnimationLeft;
    private final Animation runAnimationRight;
    private Animation currentAnimation;

    // currently only two looking directions are supported (left and right),
    // therefore a boolean is sufficient to represent the
    // looking direction
    private boolean lookLeft;

    private enum AnimationState {
        IDLE,
        RUN,
    }

    /**
     * Constructor of the Hero class.
     * <p>
     *     This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Hero() {
        String[] idleLeftFrames = new String[] {
            "tileset/knight_m_idle_left_anim_f0.png",
            "tileset/knight_m_idle_left_anim_f1.png",
            "tileset/knight_m_idle_left_anim_f2.png",
            "tileset/knight_m_idle_left_anim_f3.png"
        };
        idleAnimationLeft = createAnimation(idleLeftFrames, 6);

        String[] idleRightFrames = new String[] {
            "tileset/knight_m_idle_anim_f0.png",
            "tileset/knight_m_idle_anim_f1.png",
            "tileset/knight_m_idle_anim_f2.png",
            "tileset/knight_m_idle_anim_f3.png"
        };
        idleAnimationRight = createAnimation(idleRightFrames, 6);

        String[] runLeftFrames = new String[] {
            "tileset/knight_m_run_left_anim_f0.png",
            "tileset/knight_m_run_left_anim_f1.png",
            "tileset/knight_m_run_left_anim_f2.png",
            "tileset/knight_m_run_left_anim_f3.png"
        };
        runAnimationLeft = createAnimation(runLeftFrames, 4);

        String[] runRightFrames = new String[] {
            "tileset/knight_m_run_anim_f0.png",
            "tileset/knight_m_run_anim_f1.png",
            "tileset/knight_m_run_anim_f2.png",
            "tileset/knight_m_run_anim_f3.png"
        };
        runAnimationRight = createAnimation(runRightFrames, 4);

        lookLeft = false;
    }

    private Animation createAnimation(String[] texturePaths, int frameTime)
    {
        List<Texture> textureList = new ArrayList<>();
        for (var frame : texturePaths) {
            textureList.add(new Texture(Objects.requireNonNull(this.getClass().getResource(frame)).getPath()));
        }
        return new Animation(textureList, frameTime);
    }

    /**
     * Determine the active animation which should be played.
     * @return The active animation.
     */
    @Override
    public Animation getActiveAnimation() {
        return this.currentAnimation;
    }

    private void setCurrentAnimation(AnimationState animationState) {
        switch (animationState) {
            case RUN:
                this.currentAnimation =  lookLeft ? this.runAnimationLeft : this.runAnimationRight;
            case IDLE:
            default:
                this.currentAnimation =  lookLeft ? this.idleAnimationLeft : this.idleAnimationRight;
        }
    }

    /**
     * Get the current position in the DungeonWorld.
     * @return the current position in the DungeonWorld.
     */
    @Override
    public Point getPosition() {
        return position;
    }

    /**
     * Normalize the difference-vector between two Points on a defined basis.
     * @param p1 The origin of the vector
     * @param p2 The tip of the vector
     * @param normalizationBasis The basis on which the length of the difference-vector should be normalized.
     * @return A Point, of which the x and y members represent the components of the normalized vector.
     */
    private Point normalizeDelta(Point p1, Point p2, float normalizationBasis) {
        float diffX = p2.x - p1.x;
        float diffY = p2.y - p1.y;
        float magnitude = (float)Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

        if (magnitude != 0.0f) {
            var diffXNorm = diffX / magnitude * normalizationBasis;
            var diffYNorm = diffY / magnitude * normalizationBasis;
            return new Point(diffXNorm, diffYNorm);
        } else {
            return new Point(diffX, diffY);
        }
    }

    /**
     * Called each frame, handles movement and the switching to and back from the running animation state.
     */
    @Override
    public void update() {
        // initialize temporary point with current position
        Point newPosition = new Point(this.position);

        float movementSpeed = 0.12f;
        AnimationState animationState = AnimationState.IDLE;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newPosition.y += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newPosition.y -= movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            lookLeft = false;
            newPosition.x += movementSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            lookLeft = true;
            newPosition.x -= movementSpeed;
        }
        if (level.isTileAccessible(newPosition)) {
            // calculate normalized delta of this.position and the calculated
            // new position to avoid increased diagonal movement speed
            var normalizedDelta = normalizeDelta(this.position, newPosition, movementSpeed);

            // is the hero moving?
            if (Math.abs(normalizedDelta.x) > 0.0f ||
                Math.abs(normalizedDelta.y) > 0.0f) {
                animationState = AnimationState.RUN;
            }

            this.position.x += normalizedDelta.x;
            this.position.y += normalizedDelta.y;
        }

        setCurrentAnimation(animationState);

        this.draw();
    }

    /**
     * Override IEntity.deletable and return false for the hero.
     * @return false
     */
    @Override
    public boolean deleteable() {
        return false;
    }

    /**
     * Set reference to DungeonWorld and spawn player at random position in the level.
     */
    public void setLevel(DungeonWorld level) {
        this.level = level;
        findRandomPosition();
    }

    /**
     * Sets the current position of the Hero to a random position inside the DungeonWorld.
     */
    public void findRandomPosition() {
        this.position = new Point(level.getRandomPointInDungeon());
    }
}