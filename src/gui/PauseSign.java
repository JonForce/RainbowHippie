package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Tickable;

public class PauseSign extends GuiImage implements Tickable {
	
	public boolean hasReleased = true;
	public boolean active = true;
	
	public PauseMenu activePauseMenu;

	public PauseSign() {
		super(AssetManager.pause, new Vector2(Game.screenSize.x - 62, Game.screenSize.y - 63));
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
					if (!Game.activeGame.isPaused) {
						Game.activeGame.pause();
						activePauseMenu = new PauseMenu();
					} else {
						assert activePauseMenu != null;
						Game.activeGame.resume();
						activePauseMenu.dispose();
					}
				}
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}
}
