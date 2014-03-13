package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.Game;
import core.Renderable;
import core.Tickable;

public class GuiImage implements Renderable, Tickable {
	
	public Vector2 location;
	public Texture texture;
	public boolean visible = true;
	
	private float fadingRate;
	private float alpha = 1;
	
	public GuiImage(Texture texture, Vector2 location) {
		this.texture = texture;
		this.location = location;
		Game.activeGame.toBeRendered.add(this);
	}
	
	public GuiImage() { }
	
	public void fadeAway(float rate) {
		fadingRate = rate;
		Game.activeGame.toBeTicked.add(this);
	}
	
	public void dispose() {
		Game.activeGame.toBeRendered.remove(this);
	}
	
	@Override
	public void render() {
		if (visible) {
			Color color = getCurrentGameColor();
			
			float oldAlpha = getCurrentGameAlpha();
			color.a = oldAlpha*alpha;
			Game.activeGame.batch.setColor(color);
			
			Game.activeGame.batch.draw(texture, location.x, location.y);
			
			color.a = oldAlpha;
			Game.activeGame.batch.setColor(color);
		}
	}

	@Override
	public void tick() {
		if (alpha > .05) {
			//Needs to fade more
			alpha -= fadingRate;
		} else {
			//Done fading, stop ticking and rendering this
			Game.activeGame.toBeTicked.remove(this);
			Game.activeGame.toBeRendered.remove(this);
		}
	}
	
	private Color getCurrentGameColor() {
		return Game.activeGame.batch.getColor();
	}
	
	private float getCurrentGameAlpha() {
		Color color = Game.activeGame.batch.getColor();
		return color.a;
	}
}
