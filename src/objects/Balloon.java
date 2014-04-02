package objects;

import gui.GuiImage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.RainbowHippie;
import core.RainbowRay;
import core.Renderable;
import core.Tickable;

public class Balloon implements Renderable, Tickable {
	
	public Color color;
	
	public Vector2 location = new Vector2();
	public float moveSpeed;
	public float verticalAmountModifier = 100, verticalSpeedModifier = 100;
	
	public Texture activeTexture;
	public int srcX, srcY, srcWidth, srcHeight;
	private int frame = 0, frameDelay = 5;
	
	public boolean isSwerving;
	
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
	
	/**
	 * If at least half of the balloon is visible on the screen.
	 */
	public boolean onScreen() {
		return (location.x+(activeTexture.getWidth()/7) < Game.screenSize.x);
	}
	
	public void pop() {
		if (!onScreen())
			return;
		
		if (color.equals(Color.BLACK)) {
			Game.activeGame.scoreCounter.score -= 2;
			new GuiImage(AssetManager.minusTwo, new Vector2(location.x-50, location.y+100)).fadeAway(.1f);
			if (Game.generator.nextBoolean())
				location.y -= 100;
			else
				location.y += 100;
			
			if (Game.activeGame.scoreCounter.score <= 1)
				Game.activeGame.hippie.die();
		} else {
			Game.activeGame.scoreCounter.score ++;
			dispose();
			new GuiImage(AssetManager.plusOne, new Vector2(location.x-50, location.y+100)).fadeAway(.1f);
		}
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
		
		if (RainbowRay.isPopping(this) && RainbowHippie.activeHippie.isRainbowing)
			pop();
		
		if(location.x < -(AssetManager.balloon.getWidth()/7)) {
			if (!color.equals(Color.BLACK)) {
				// If the player's score is below 0, kill him
				if (Game.activeGame.scoreCounter.score > 1)
					Game.activeGame.scoreCounter.score -=2;
				else
					Game.activeGame.hippie.die();
				
				new GuiImage(AssetManager.minusTwo, new Vector2(10, location.y+100)).fadeAway(.1f);
			}
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