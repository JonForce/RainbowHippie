package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
	
	//Hippie junk
	public static Texture fly;
	public static Texture flyHold;
	
	//GUI
	public static Texture sign;
	public static Texture logo;
	public static Texture font;
	public static Texture quitButton;
	
	//Background images
	public static Texture bg;
	public static Texture mountain;
	public static Texture rays;
	public static Texture cloudA;
	public static Texture cloudB;
	public static Texture cloudC;
	
	//Objects
	public static Texture barrel;
	public static Texture balloon;
	
	//Effects
	public static Texture gasTrail;
	public static Texture rainbow;
	
	public static void loadAssets() {
		fly = new Texture(Gdx.files.internal("assets/sprite/fly.png"));
		flyHold = new Texture(Gdx.files.internal("assets/sprite/flyhold.png"));
		
		sign = new Texture(Gdx.files.internal("assets/sprite/signfloat.png"));
		logo = new Texture(Gdx.files.internal("assets/logo.png"));
		font = new Texture(Gdx.files.internal("assets/sprite/font.png"));
		quitButton = new Texture(Gdx.files.internal("assets/ui/ragequit.png"));
		
		bg = new Texture(Gdx.files.internal("assets/bg/bg.png"));
		mountain = new Texture(Gdx.files.internal("assets/bg/mountain.png"));
		rays = new Texture(Gdx.files.internal("assets/bg/rayoflight.png"));
		cloudA = new Texture(Gdx.files.internal("assets/bg/cloud00.png"));
		cloudB = new Texture(Gdx.files.internal("assets/bg/cloud01.png"));
		cloudC = new Texture(Gdx.files.internal("assets/bg/cloud02.png"));
		
		barrel = new Texture(Gdx.files.internal("assets/sprite/barrel.png"));
		balloon = new Texture(Gdx.files.internal("assets/sprite/barrel.png"));
		
		gasTrail = new Texture(Gdx.files.internal("assets/sprite/Gas_Trail.png"));
		rainbow = new Texture(Gdx.files.internal("assets/sprite/rainbow.png"));
	}
}