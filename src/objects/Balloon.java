package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

public class Balloon implements Renderable, Tickable {
	
	public Color color;
	
	private Vector2 location;
	private float moveSpeed;
	
	public Texture activeTexture;
	public int srcX, srcY, srcWidth, srcHeight;
	private int frame = 0, frameDelay = 5;
	
	public Balloon(Color color) {
		this.color = new Color(color);
		this.location = new Vector2(Game.generator.nextInt((int) Game.screenSize.x), -200);
		//See if balloon is spawned too close to player. May need to tweak
		this.activeTexture = AssetManager.balloon;
		if(location.x <= Game.center.x - 200) location.x = Game.center.x - 200;
		if(location.x >= Game.screenSize.x - activeTexture.getWidth() / 7) location.x = Game.screenSize.x - activeTexture.getWidth() / 7;
		this.moveSpeed = 25 * Game.generator.nextFloat() + 5f;
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.toBeRendered.add(this);
	}

	@Override
	public void tick() {
		animate(6, activeTexture);
		location.y += moveSpeed;
		
		if(location.y >= Game.screenSize.y) {
			Game.activeGame.toBeRendered.remove(this);
			Game.activeGame.toBeTicked.remove(this);
		}
	}

	@Override
	public void render() {
		Game.activeGame.batch.setColor(color);
		Game.activeGame.batch.draw(activeTexture, location.x, location.y, srcX, srcY, srcWidth, srcHeight);
		Game.activeGame.batch.setColor(Color.WHITE);
	}

	/*
	 * Sets the activeSpritesheet, srcX, width and height.
	 */
	private void animate(int frames, Texture spriteSheet) {
		if (frameDelay <= 0) {
			frameDelay = 2;
			if (frame >= frames - 1)
				frame = 0;
			else
				frame++;

			activeTexture = spriteSheet;
			srcX = frame * (spriteSheet.getWidth() / frames);
			srcY = 0;
			srcWidth = (spriteSheet.getWidth() / frames);
			srcHeight = spriteSheet.getHeight();
		}
		frameDelay--;
		
	}
}
