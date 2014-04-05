package aesthetics;

import gui.GuiImage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import core.AssetManager;
import core.Game;
import core.Renderable;
import core.Tickable;

public class Background {
	
	public static GuiImage bg;
	public static GuiImage mountain;
	public static GuiImage rays;
	
	private static Tickable updater;
	private static float locationA, locationB, locationC;
	private static float speedA, speedB, speedC;
	private static float acceleration = 0, width;
	
	public static void load() {
		locationA = locationB = locationC = Game.screenSize.x/2;
		width = Game.screenSize.x;
	}
	
	public static void renderBackground() {
		// Render the blue backdrop
		Game.activeGame.batch.draw(AssetManager.bg, 0, 0, Game.screenSize.x, Game.screenSize.y);
		
		// Render the mountain next, over the backdrop
		Game.activeGame.batch.draw(AssetManager.mountain, 0, 0);
		
		Game.activeGame.batch.draw(AssetManager.cloudC, locationC, 0, width, AssetManager.cloudC.getHeight());
		Game.activeGame.batch.draw(AssetManager.cloudC, locationC-width, 0, width, AssetManager.cloudC.getHeight());
		
		Game.activeGame.batch.draw(AssetManager.cloudB, locationB, 0, width, AssetManager.cloudB.getHeight());
		Game.activeGame.batch.draw(AssetManager.cloudB, locationB-width, 0, width, AssetManager.cloudB.getHeight());
	}
	
	public static void renderForeground() {
		Game.activeGame.batch.draw(AssetManager.cloudA, locationA, 0, width, AssetManager.cloudA.getHeight());
		Game.activeGame.batch.draw(AssetManager.cloudA, locationA-width, 0, width, AssetManager.cloudA.getHeight());
	}
	
	public static void startMovingClouds() {
		acceleration = -.9f;
		updater = new Tickable() {
			@Override
			public void tick() {
				acceleration *= .9;
				
				// Makes the clouds move slower by layer, giving illusion of depth
				speedA += acceleration*2;
				speedB += acceleration*1.5;
				speedC += acceleration;
				
				// There is no reason for the clouds to move in the wrong direction
				assert speedA > 0 && speedB > 0 && speedC > 0;
				
				locationA += speedA;
				locationB += speedB;
				locationC += speedC;
				
				if (locationA <= 0)
					locationA = Game.screenSize.x;
				if (locationB <= 0)
					locationB = Game.screenSize.x;
				if (locationC <= 0)
					locationC = Game.screenSize.x;
			}
		};
		Game.activeGame.toBeTicked.add(updater);
	}
}