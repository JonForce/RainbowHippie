package aesthetics;

import gui.GuiImage;

import com.badlogic.gdx.math.Vector2;

import core.AssetManager;

public class Background {
	
	public static GuiImage bg;
	public static GuiImage mountain;
	public static GuiImage rays;
	public static RecursiveImage[] cloudsA;
	public static RecursiveImage[] cloudsB;
	
	public static void load() {
		bg = new GuiImage(AssetManager.bg, new Vector2(0,0));
		mountain = new GuiImage(AssetManager.mountain, new Vector2(0,0));
		
		//rays = new GuiImage(AssetManager.rays, new Vector2(0,0));
		
		cloudsA = new RecursiveImage[3];
		cloudsB = new RecursiveImage[3];
		
		cloudsA[2] = new RecursiveImage(AssetManager.cloudC, new Vector2(-.5f*AssetManager.cloudC.getWidth(),0), -.1f);
		cloudsB[2] = new RecursiveImage(AssetManager.cloudC, new Vector2(.5f*AssetManager.cloudC.getWidth(),0), -.1f);
		
		cloudsA[1] = new RecursiveImage(AssetManager.cloudB, new Vector2(-.5f*AssetManager.cloudB.getWidth(),0), -.15f);
		cloudsB[1] = new RecursiveImage(AssetManager.cloudB, new Vector2(.5f*AssetManager.cloudB.getWidth()-2,0), -.15f);
		
		cloudsA[0] = new RecursiveImage(AssetManager.cloudA, new Vector2(-.5f*AssetManager.cloudA.getWidth(),0), -.2f);
		cloudsB[0] = new RecursiveImage(AssetManager.cloudA, new Vector2(.5f*AssetManager.cloudA.getWidth()+7,0), -.2f);
	}
	
	public static void startMovingClouds() {
		cloudsA[0].accelerateTo(-3.5f, 1.1f);
		cloudsB[0].accelerateTo(-3.5f, 1.1f);
		cloudsA[1].accelerateTo(-2.75f, 1.1f);
		cloudsB[1].accelerateTo(-2.75f, 1.1f);
		cloudsA[2].accelerateTo(-2f, 1.1f);
		cloudsB[2].accelerateTo(-2f, 1.1f);
	}
}
