package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Tickable;

public class StartSign extends GuiImage implements Tickable {
	
	public int srcX, srcY, srcWidth, srcHeight;
	public boolean hasReleased = true;
	public boolean active = true;
	
	public StartSign() {
		super(AssetManager.sign, new Vector2(Game.startPosition.x+93, Game.startPosition.y+80));
		Game.activeGame.toBeTicked.add(this);
	}
	
	public void useFrame(int num) {
		srcX = num*(texture.getWidth()/7);
		srcY = 0;
		srcWidth = (texture.getWidth()/7);
		srcHeight = texture.getHeight();
	}
	
	public void disable() {
		visible = false;
		active = false;
	}
	
	public void enable() {
		visible = true;
		active = true;
	}
	
	@Override
	public void render() {
		if (visible)
			Game.activeGame.batch.draw(texture, location.x, location.y,
										srcX, srcY, srcWidth, srcHeight);
	}
	
	@Override
	public void tick() {
		//This logic gate will run the code within the "if (hasReleased)" statement once per click
		if (Gdx.input.isTouched() && active) {
			if (hasReleased) {
				//Check if the click was in the area of the screen that triggers the start of the game
				if (Gdx.input.getX() >= Game.center.x-100 && Gdx.input.getX() <= Game.center.x+55 &&
						Game.screenSize.y - Gdx.input.getY() >= Game.center.y-85 && Game.screenSize.y - Gdx.input.getY() <= Game.center.y-10) {
					//Start the game
					Game.activeGame.start();
				}
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}
}