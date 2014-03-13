package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Tickable;

public class PauseMenu implements Tickable {
	
	private static Vector2 center = new Vector2(Game.screenSize.x/2, Game.screenSize.y/2);
	private static Vector2 buttonSize = new Vector2(AssetManager.resumeButton.getWidth(), AssetManager.resumeButton.getHeight());
	public GuiImage window, resumeButton, restartButton, quitButton;
	
	private static int buttonVerticalSpacing = 5;
	private boolean hasReleased;
	
	public PauseMenu() {
		window = new GuiImage(AssetManager.pauseWindow, new Vector2(center.x-(AssetManager.pauseWindow.getWidth()/2), center.y-(AssetManager.pauseWindow.getHeight()/2)));
		resumeButton = new GuiImage(AssetManager.resumeButton, new Vector2(center.x-(buttonSize.x/2), center.y+30));
		restartButton = new GuiImage(AssetManager.restartButton, new Vector2(resumeButton.location.x, resumeButton.location.y-buttonSize.y-buttonVerticalSpacing));
		quitButton = new GuiImage(AssetManager.quitButton, new Vector2(restartButton.location.x, restartButton.location.y-buttonSize.y-buttonVerticalSpacing));
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.pausedTicked.add(this);
	}
	
	public void dispose() {
		Game.activeGame.toBeTicked.remove(this);
		Game.activeGame.pausedTicked.remove(this);
		window.dispose();
		resumeButton.dispose();
		restartButton.dispose();
		quitButton.dispose();
	}

	@Override
	public void tick() {
		//This logic gate will run the code within the "if (hasReleased)" statement once per click
		if (Gdx.input.isTouched()) {
			if (hasReleased) {
				
				//If click or touch is within the area of the three buttons
				int x = Gdx.input.getX();
				int y = (int) (Game.screenSize.y-Gdx.input.getY());
				if (x >= center.x-(buttonSize.x/2) && x <= center.x+(buttonSize.x/2) &&
						y >= center.y-(buttonSize.y*1.5)-(buttonVerticalSpacing*2) &&
						y <= center.y+(buttonSize.y*1.5)+(buttonVerticalSpacing*2)) {
					//Check which button was pressed, from top to bottom
					if (y >= resumeButton.location.y) {
						this.dispose();
						Game.activeGame.resume();
					} else if (y >= restartButton.location.y) {
						Game.activeGame.restart();
						this.dispose();
						Game.activeGame.resume();
					} else if (y >= quitButton.location.y) {
						Game.activeGame.quit();
					}
				}
				
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}
}
