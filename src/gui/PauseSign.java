package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Tickable;

public class PauseSign extends GuiImage implements Tickable {
	
	public boolean hasReleased = true;
	public boolean active = true;
	public boolean isPaused = false;

	public PauseSign() {
		super(AssetManager.pause, new Vector2(Game.screenSize.x - 75, Game.screenSize.y - 65));
		Game.activeGame.toBeTicked.add(this);
	}

	public void enable() {
		visible = true;
		active = true;
	}
	
	public void disable() {
		visible = false;
		active = false;
	}
	
	@Override
	public void render() {
		//If visible, render the texture (AssetManager.pause) at the specified location with the entire width and height of the texture because it is not animated
		if(visible) 
			Game.activeGame.batch.draw(texture, location.x, location.y, 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	@Override
	public void tick() {
		if(Gdx.input.isTouched() && active) {
			if(hasReleased) {
				if(Gdx.input.getX() >= location.x && Gdx.input.getX() <= location.x + texture.getWidth() &&
						Game.screenSize.y - Gdx.input.getY() >= location.y && Game.screenSize.y - Gdx.input.getY() <= location.y + texture.getHeight()) {
					isPaused = !isPaused;
				}
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}
}
