package aesthetics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.Game;
import core.Renderable;
import core.Tickable;

public class RecursiveImage implements Renderable, Tickable {
	
	public boolean visible = true;
	public Texture texture;
	public Vector2 location;
	public float movement;
	public float limitPositiveX, limitNegitiveX;
	
	private float acceleratingTo = 0;
	private float acceleratingRate = 0;
	
	public RecursiveImage(Texture texture, Vector2 startLocation, float movement) {
		this.texture = texture;
		this.location = startLocation;
		this.movement = movement;
		limitPositiveX = Game.screenSize.x+texture.getWidth();
		limitNegitiveX = -texture.getWidth();
		Game.activeGame.toBeRendered.add(this);
	}
	
	public void accelerateTo(float accelerateTo, float rate) {
		acceleratingTo = accelerateTo;
		acceleratingRate = rate;
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void render() {
		//Update image location
		location.x += movement;
		if (location.x <= limitNegitiveX) {
			location.x = Game.screenSize.x;
		} else if (location.x >= limitPositiveX) {
			location.x = -texture.getWidth();
		}
		
		//Render
		if (visible) {
			Game.activeGame.batch.draw(texture, location.x, location.y);
		}
	}
	
	public void stopAccelerating() {
		Game.activeGame.toBeTicked.remove(this);
	}
	
	@Override
	public void tick() {
		if (movement < 0) {
			if (movement >= acceleratingTo) {
				movement *= acceleratingRate;
			} else {	stopAccelerating();		}
		} else if (movement > 0) {
			if (movement <= acceleratingTo) {
				movement *= acceleratingRate;
			} else {	stopAccelerating();		}
		} else {
			System.err.println("Cannot smoothly accelerate to anything if the movement parameter was set to 0");
			stopAccelerating();
		}
	}
}