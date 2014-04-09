package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

/**
 * @author Jon
 *	Used when the Hippie dies to send body parts flying left offscreen.
 */
public class FlyingBodyPart implements Renderable, Tickable {
	
	public static final int HEAD = 0, BODY = 1, LEG = 2, ARM = 3;
	
	public Vector2 velocity = new Vector2();
	public float torque;
	private Texture texture;
	private Sprite sprite;
	
	private Vector2 location;
	private float rotation = 0, alpha = 1;
	
	public FlyingBodyPart(Vector2 location, int type) {
		if (type == 0) { sprite = new Sprite(AssetManager.head); texture = AssetManager.head; }
		else if (type == 1) { sprite = new Sprite(AssetManager.body); texture = AssetManager.body; }
		else if (type == 2) { sprite = new Sprite(AssetManager.leg); texture = AssetManager.leg; }
		else if (type == 3) { sprite = new Sprite(AssetManager.arm); texture = AssetManager.arm; }
		this.location = new Vector2(location);
		
		sprite.setOrigin(texture.getWidth()/2, texture.getHeight()/2);
		sprite.setPosition(location.x, location.y);
		
		//Generate random speeds
		velocity.x = -Game.generator.nextInt(30);
		velocity.y = Game.generator.nextInt(10);
		torque = Game.generator.nextInt(20);
		
		//Begin
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.toBeRendered.add(this);
	}
	
	public void tick() {
		location.x += velocity.x;
		location.y += velocity.y;
		rotation += torque;
		alpha *= .75f;
		
		sprite.setPosition(location.x, location.y);
		sprite.setRotation(rotation);
		
		//Destroy if invisible
		if (alpha < .05) {
			Game.activeGame.toBeTicked.remove(this);
			Game.activeGame.toBeRendered.remove(this);
		}
	}

	@Override
	public void render() {
		sprite.draw(Game.activeGame.batch, alpha);
	}
}
