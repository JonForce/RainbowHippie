package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.RainbowHippie;
import core.Renderable;
import core.Tickable;

public class Instructions implements Tickable, Renderable {
	
	public Vector2 renderLocation = new Vector2((Game.screenSize.x/2)-(AssetManager.tip1.getWidth()/2), (Game.screenSize.y/2)-(AssetManager.tip1.getHeight()/2));
	public int stage = 0;
	
	private boolean hasIncreasedStage = false;
	
	public Instructions() {
		Game.activeGame.toBeRendered.add(this);
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void tick() {
		if (Gdx.input.isTouched() && !hasIncreasedStage) {
			stage ++;
			hasIncreasedStage = true;
		}
		
		if (!Gdx.input.isTouched())
			hasIncreasedStage = false;
	}
	
	@Override
	public void render() {
		assert stage >= 0;
		if (stage <= 2) {
			if (stage == 0)
				Game.activeGame.batch.draw(AssetManager.tip1, renderLocation.x, renderLocation.y);
			if (stage == 1)
				Game.activeGame.batch.draw(AssetManager.tip2, renderLocation.x, renderLocation.y);
			if (stage == 2)
				Game.activeGame.batch.draw(AssetManager.tip3, renderLocation.x, renderLocation.y);
		} else {
			Game.activeGame.hippie = new RainbowHippie();
			Game.activeGame.toBeRendered.remove(this);
			Game.activeGame.toBeTicked.remove(this);
			Game.prefs.putBoolean("isFirstLaunch", false);
			Game.prefs.flush();
		}
	}
}