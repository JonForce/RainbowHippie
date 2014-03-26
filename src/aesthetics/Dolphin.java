package aesthetics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

public class Dolphin extends Sprite implements Tickable, Renderable {
	
	public Vector2 location;
	public float rotation;
	
	public float peakY, torque;
	
	public Dolphin() {
		super(AssetManager.dolphin);
		location = new Vector2(Game.generator.nextInt(100), -10);
		rotation = 45f;
		torque = -1.5f;
		peakY = 100+Game.generator.nextInt(60);
		update();
		
		Game.activeGame.toBeRendered.remove(Background.cloudsA[0]);
		Game.activeGame.toBeRendered.remove(Background.cloudsB[0]);
		Game.activeGame.toBeRendered.add(this);
		Game.activeGame.toBeRendered.add(Background.cloudsA[0]);
		Game.activeGame.toBeRendered.add(Background.cloudsB[0]);
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void tick() {
		rotation += torque;
		location.x += 25f;
		location.y = (float) -Math.pow(location.x-Game.screenSize.x/2, 2)*.0005f+peakY; // y value is the output of this parabolic function
		
		update();
	}
	
	private void update() {
		this.setPosition(location.x, location.y);
		this.setRotation(rotation);
	}
	
	@Override
	public void render() {
		this.draw(Game.activeGame.batch);
	}
	
}