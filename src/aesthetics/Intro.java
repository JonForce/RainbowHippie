package aesthetics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import core.AssetManager;
import core.Game;
import core.Renderable;

public class Intro implements Renderable {

	public static boolean finished = false;

	private Texture currentFrame;
	private int frame = 0, nextFrame;

	private void load(int frame) {
		if ((frame >= 2 && frame <= 12) || (frame >= 51 && frame <= 53) || (frame >= 92 && frame <= 118) || frame >= 134) return;
		else {
			currentFrame = new Texture(Gdx.files.internal("assets/ui/intro/Frame (" + frame + ").jpg"));
			this.frame++;
		}
	}

	public void play() {
		Game.activeGame.toBeRendered.add(this);
		AssetManager.introSong.play(.1f);
	}

	@Override
	public void render() {
		if (!finished) {
			
			nextFrame = frame + 1;
			load(nextFrame);
			Game.activeGame.batch.draw(currentFrame, 0, 0, Game.screenSize.x, Game.screenSize.y);
			
			if(frame >= 134) finished = true;
		} else {
			Game.activeGame.createMenu();
			Game.activeGame.toBeRendered.remove(this);
		}
	}
}
