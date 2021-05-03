package items;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Item implements IAnimatable, IEntity{
  protected final Game game = Game.getInstance();
  protected DungeonWorld level;
  protected Animation currentAnimation;
  protected Point position;
  public boolean isStackable() {
    return true;
  }
  public abstract String getName();
  protected abstract String getDescription();
  public Item(){}
  @Override
  public void update(){
    this.draw();
  }
  @Override
  public boolean deleteable(){
    return false;
  }

  @Override
  public Animation getActiveAnimation() {
    return this.currentAnimation;
  }
  public void findRandomPosition() {
    this.position = new Point(level.getRandomPointInDungeon());
  }

  public void setLevel(DungeonWorld level){
    this.level = level;
  findRandomPosition();
  }
  protected Animation createAnimation(String[] texturePaths, int frameTime) {
    List<Texture> textureList = new ArrayList<>();
    for (var frame : texturePaths) {
      textureList.add(new Texture(Objects.requireNonNull(this.getClass().getClassLoader().getResource(frame)).getPath()));
    }
    return new Animation(textureList, frameTime);
  }

  public void accept(IItemVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Point getPosition() {
    return position;
  }


  public void setPosition(Point newPosition){
    this.position = newPosition;
  }
}
