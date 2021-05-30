package mock;

import main.Combatable;

// is always dead
public class MockCombatable implements Combatable {
  @Override
  public float getHealth() {
    return 50;
  }

  @Override
  public void setHealth(float health) {}

  @Override
  public void heal(float amount) {}

  @Override
  public boolean hasTarget() {
    return false;
  }

  @Override
  public Combatable getTarget() {
    return null;
  }

  @Override
  public void setTarget(Combatable target) {}

  @Override
  public boolean isOtherFriendly(Combatable other) {
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
