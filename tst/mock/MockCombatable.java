package mock;

import main.ICombatable;

// is always dead
public class MockCombatable implements ICombatable {
    @Override
    public float getHealth() {
        return 50;
    }

    @Override
    public void setHealth(float health) {

    }

    @Override
    public void heal(float amount) {

    }

    @Override
    public boolean hasTarget() {
        return false;
    }

    @Override
    public ICombatable getTarget() {
        return null;
    }

    @Override
    public void setTarget(ICombatable target) {

    }

    @Override
    public boolean isOtherFriendly(ICombatable other) {
        return false;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public float getHitChance() {
        return 0;
    }

    @Override
    public float getEvasionChance() {
        return 0;
    }

    @Override
    public float getDamage() {
        return 0;
    }

    @Override
    public boolean isDead() {
        return true;
    }
}
