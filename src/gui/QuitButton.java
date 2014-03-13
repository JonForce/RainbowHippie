package gui;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;

public class QuitButton extends GuiImage {
	
	public boolean hasReleased = true;
	
	public QuitButton() {
		super(AssetManager.quitButton, new Vector2(1000, Game.screenSize.y-AssetManager.quitButton.getHeight()));
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void tick() {
		//This logic gate will run the code within the "if (hasReleased)" statement once per click
		if (Mouse.isButtonDown(0)) {
			if (hasReleased) {
				//Check if the click was in the area of the screen that triggers quit
				if (Mouse.getX() >= 1000 && Mouse.getX() <= 1000+AssetManager.quitButton.getWidth() &&
						Mouse.getY() >= location.y && Mouse.getY() <= Game.screenSize.y) {
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