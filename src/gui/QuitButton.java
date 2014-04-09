package gui;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;

public class QuitButton extends GuiImage {
	
	public boolean hasReleased = true;
	
	public QuitButton() {
		super(AssetManager.quitCloud, new Vector2(1000, Game.screenSize.y-AssetManager.quitCloud.getHeight()));
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void tick() {
		//This logic gate will run the code within the "if (hasReleased)" statement once per click
		if (Gdx.input.isTouched()) {
			if (hasReleased) {
				//Check if the click/touch was in the area of the screen that triggers quit
				if(Gdx.input.getX() >= location.x && Gdx.input.getX() <= location.x + texture.getWidth() &&
						Game.screenSize.y - Gdx.input.getY() >= location.y && Game.screenSize.y - Gdx.input.getY() <= location.y + texture.getHeight()) {
					//Quit the game
					Gdx.app.exit();
				}
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}
}