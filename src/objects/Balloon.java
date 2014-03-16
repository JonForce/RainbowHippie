package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.RainbowHippie;
import core.Renderable;
import core.Tickable;

public class Balloon implements Renderable, Tickable {
	
	public Color color;
	
	private Vector2 location = new Vector2();
	private float moveSpeed;
	public float verticalAmountModifier = 100, verticalSpeedModifier = 100;
	
	public Texture activeTexture;
	public int srcX, srcY, srcWidth, srcHeight;
	private int frame = 0, frameDelay = 5;
	
	private boolean isSwerving, behindPlayer;
	
	public Balloon(Color color) {
		this.color = new Color(color);
		activeTexture = AssetManager.balloon;
		location.x = Game.screenSize.x + (activeTexture.getWidth()/6); // 6 is the number of frames
		moveSpeed = 10 * Game.generator.nextFloat() + 5f;
		
		if (Game.generator.nextBoolean()) {
			location.y = (Game.screenSize.y/2)-(activeTexture.getHeight()/2);
			verticalAmountModifier = 10 + Game.generator.nextInt(25);
			verticalSpeedModifier = verticalAmountModifier + Game.generator.nextInt(5);
			isSwerving = true;
		} else {
			location.y = Game.generator.nextInt((int) Game.screenSize.y - activeTexture.getHeight());
			verticalAmountModifier = 0;
			verticalSpeedModifier = 0;
			isSwerving = false;
		}
		
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.toBeRendered.add(this);
	}
	
	public Vector2 getCenter() {
		return new Vector2(location.x+(activeTexture.getWidth()/2), location.y+(activeTexture.getHeight()/2));
	}
	
	public void pop() {
		Game.activeGame.scoreCounter.addOne();
		dispose();
	}
	
	public void dispose() {
		Game.activeGame.toBeRendered.remove(this);
		Game.activeGame.toBeTicked.remove(this);
	}
	
	@Override
	public void tick() {
		animate(6, activeTexture);
		location.x -= moveSpeed;
		if (isSwerving)
			location.y += (float) (Math.sin(location.x/verticalAmountModifier) * verticalSpeedModifier);
		
		if (RainbowHippie.activeHippie.rainbowCollisionTest.test(getCenter()) && RainbowHippie.activeHippie.isRainbowing && !color.equals(Color.WHITE))
			pop();
		
		if(location.x < Game.activeGame.hippie.location.x && !behindPlayer) {
			Game.activeGame.scoreCounter.subtractOne();
			behindPlayer = true;
		}
		
		if(location.x <= 0-activeTexture.getWidth()) {
			dispose();
		}
	}

	@Override
	public void render() {
		Game.activeGame.batch.setColor(color);
		Game.activeGame.batch.draw(activeTexture, location.x, location.y, srcX, srcY, srcWidth, srcHeight);
		Game.activeGame.batch.setColor(Color.WHITE);
	}

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
