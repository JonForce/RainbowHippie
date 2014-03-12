package aesthetics;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

public class Partical implements Renderable, Tickable {
	
	public static final Random generator = new Random();
	
	public Vector2 location;
	public float rotation;
	public Rectangle boundingBox;
	public float torque;
	public float velocity;
	
	public Sprite sprite;
	private Texture texture;
	public int frames;
	private int frame = 0;
	
	public Partical(float x, float y, Texture texture, int frames) {
		location = new Vector2(x,y);
		sprite = new Sprite(texture);
		this.frames = frames;
		this.texture = texture;
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.toBeRendered.add(this);
		
		torque = randomInt(7, 20);
		velocity = randomInt(-10, 10);
	}
	
	@Override
	public void render() {
		sprite.setPosition(location.x, location.y);
		sprite.setRotation(rotation);
		sprite.setScale(.1f,1);
		sprite.setRegion(frame*(texture.getWidth()/frames), 0, texture.getWidth()/frames, texture.getHeight());
		sprite.draw(Game.activeGame.batch);
	}
	
	@Override
	public void tick() {
		if (frame >= frames-1) {
			frame = 0;
		} else {
			frame ++;
		}
	}
	
	public static int randomInt(int min, int max) {
		return generator.nextInt(max-min) + min;
	}
	
	public static float random(float min, float max) {
		return  generator.nextInt((int)max-(int)min) + min + generator.nextFloat();
	}
}