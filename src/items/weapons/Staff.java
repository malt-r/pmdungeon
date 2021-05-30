package items.weapons;

import items.IItemVisitor;
import stats.Attribute;
import stats.Modifier;

/**
 * Staff class.
 * <p>
 *   Contains everything that describes a staff.
 * </p>
 */
public class Staff extends Weapon {
    /**
     * Constructor of the Staff class.
     * <p>
     * This constructor will instantiate the animations and read all required texture data.
     * </p>
     */
    public Staff(){
        super();
        this.modifiers.add(new Modifier(1.2f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.PHYSICAL_ATTACK_DAMAGE));
        this.modifiers.add(new Modifier(1.2f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.HIT_CHANCE));
        //this.attackDamageModifier=1.2f;
        //this.hitChanceModifier=1.2f;
        this.range=3.0f;
        // TODO: use health for that
        this.condition=2000;
        String[] idleLeftFrames = new String[]{
                "tileset/items/weapon_red_magic_staff.png"
        };
        currentAnimation = createAnimation(idleLeftFrames, Integer.MAX_VALUE);

    }
    public float getRange(){ return this.range; }
    /**
     * Returns the name of the staff for display purposes
     * @return Name of the staff
     */
    @Override
    public String getName() {
        return "Magic Staff";
    }
    /**
     * description of the staff for display purposes
     * @return description of the staff
     */
    @Override
    protected String getDescription() {
        return "Staff used for ranged combat";
    }
    /**
     * Called each frame and draws the staff with the right scaling.
     */
    @Override
    public void update(){
        draw(-0.75f,-0.75f,0.5f,1.0f);
    }
    /**
     * Accept method for a item visitor to extend the functionality of the staff.
     * @param visitor Visitor that visits the class
     */
    @Override
    public void accept(IItemVisitor visitor){
        visitor.visit(this);
    }
}