package aesthetics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import core.AssetManager;
import core.Game;
import core.Renderable;

public class Intro implements Renderable {

	public static boolean finished = false;

	private Texture currentFrame;
	private int frame = 0, nextFrame, endDelay = 180;

	private Texture load(int frame) {
		if(currentFrame == null) currentFrame = new Texture(Gdx.files.internal("assets/ui/intro/Frame (1).jpg" ));
		if ((frame >= 2 && frame <= 12) || (frame >= 51 && frame <= 53) || (frame >= 92 && frame <= 118) || frame >= 134) {
			this.frame++;
			return currentFrame;
		} else {
			this.frame++;
			currentFrame = new Texture(Gdx.files.internal("assets/ui/intro/Frame (" + frame + ").jpg"));
		}
		return currentFrame;
	}

	public void play() {
		Game.activeGame.toBeRendered.add(this);
		AssetManager.introSong.play(.5f);
	}

	@Override
	public void render() {
		if(!finished) {
			Game.activeGame.batch.draw(load(++frame), 0, 0, Game.screenSize.x, Game.screenSize.y);
			
			try {
				Thread.sleep(64);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(frame >= 134) {
				if(endDelay <= 0) {
					Game.activeGame.toBeRendered.remove(this);
					finished = true;
				}
				endDelay--;
			}
		}
	}
}
