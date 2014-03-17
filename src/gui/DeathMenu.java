package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

public class DeathMenu implements Tickable, Renderable {

	public GuiImage window, playAgain, facebook, twitter;
	public ScoreCounter counter, highScore;

	private boolean hasReleased = false;

	public DeathMenu() {
		window = new GuiImage(AssetManager.deathWindow, new Vector2(Game.center.x - (AssetManager.pauseWindow.getWidth() / 2), Game.center.y - (AssetManager.pauseWindow.getHeight() / 2)));
		playAgain = new GuiImage(AssetManager.playAgain, new Vector2(window.location.x + 105, window.location.y + 40));
		facebook = new GuiImage(AssetManager.facebook, new Vector2(window.location.x + 270, window.location.y + 40));
		twitter = new GuiImage(AssetManager.twitter, new Vector2(window.location.x + (270 + 60), window.location.y + 40));
		counter = new ScoreCounter(false);
		highScore = new ScoreCounter(false);
		counter.score = Game.activeGame.scoreCounter.score;
		Game.activeGame.toBeTicked.add(this);
		Game.activeGame.toBeRendered.add(this);
		
		if(Game.prefs.getInteger("high_score") < Game.activeGame.scoreCounter.score) {
			Game.prefs.putInteger("high_score", Game.activeGame.scoreCounter.score);
			Game.prefs.flush();
		}
	}

	@Override
	public void render() {
		counter.render(window.location.x + 70, window.location.y + 237);
		counter.render(Game.prefs.getInteger("high_score"), window.location.x + 70, window.location.y + 155);
	}

	@Override
	public void tick() {
		if (Gdx.input.isTouched()) {
			if (hasReleased) {
				if(playAgain.isClicked()) {
					Game.activeGame.restart();
					dispose();
				}
			}
			hasReleased = false;
		} else {
			hasReleased = true;
		}
	}

	public void dispose() {
		window.fadeAway(.1f);
		playAgain.fadeAway(.1f);
		facebook.fadeAway(.1f);
		twitter.fadeAway(.1f);
		Game.activeGame.toBeRendered.remove(counter);
		Game.activeGame.toBeTicked.remove(this);
		Game.activeGame.toBeRendered.remove(this);
	}
}
