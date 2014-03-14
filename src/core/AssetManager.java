package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
public class AssetManager {
	
	//Hippie junk
	public static Texture fly, flyHold;
	public static Texture head, body, arm, leg;
	
	//GUI
	public static Texture sign, logo;
	public static Texture pause, pauseWindow, resumeButton, restartButton, quitButton, quitCloud;
	public static Texture font;
	public static Texture[] intro;
	
	//Background images
	public static Texture bg, mountain, rays;
	public static Texture cloudA, cloudB, cloudC;
	
	//Objects
	public static Texture barrel, balloon;
	
	//Effects
	public static Texture gasTrail, rainbow;
	public static Music bgMusic;
	
	public static void loadAssets() {
		/*
		intro = new Texture[133];
		for (int i = 1; i != 133; i ++) {
			intro[i] = new Texture(Gdx.files.internal("assets/intro/Frame ("+i+").jpg"));
		}
		*/
		
		fly = new Texture(Gdx.files.internal("assets/sprite/fly.png"));
		flyHold = new Texture(Gdx.files.internal("assets/sprite/flyhold.png"));
		body = new Texture(Gdx.files.internal("assets/sprite/body.png"));
		head = new Texture(Gdx.files.internal("assets/sprite/head.png"));
		arm = new Texture(Gdx.files.internal("assets/sprite/arm.png"));
		leg = new Texture(Gdx.files.internal("assets/sprite/leg.png"));
		
		sign = new Texture(Gdx.files.internal("assets/sprite/signfloat.png"));
		logo = new Texture(Gdx.files.internal("assets/logo.png"));
		font = new Texture(Gdx.files.internal("assets/sprite/font.png"));
		pause = new Texture(Gdx.files.internal("assets/ui/pause.png"));
		pauseWindow = new Texture(Gdx.files.internal("assets/ui/pausewindow.png"));
		resumeButton = new Texture(Gdx.files.internal("assets/ui/resume.png"));
		restartButton = new Texture(Gdx.files.internal("assets/ui/restart.png"));
		quitButton = new Texture(Gdx.files.internal("assets/ui/rage quit.png"));
		
		quitCloud = new Texture(Gdx.files.internal("assets/ui/ragequit.png"));
		
		bg = new Texture(Gdx.files.internal("assets/bg/bg.png"));
		mountain = new Texture(Gdx.files.internal("assets/bg/mountain.png"));
		rays = new Texture(Gdx.files.internal("assets/bg/rayoflight.png"));
		cloudA = new Texture(Gdx.files.internal("assets/bg/cloud00.png"));
		cloudB = new Texture(Gdx.files.internal("assets/bg/cloud01.png"));
		cloudC = new Texture(Gdx.files.internal("assets/bg/cloud02.png"));
		
		barrel = new Texture(Gdx.files.internal("assets/sprite/barrel.png"));
		balloon = new Texture(Gdx.files.internal("assets/sprite/balloon.png"));
		
		gasTrail = new Texture(Gdx.files.internal("assets/sprite/Gas_Trail.png"));
		rainbow = new Texture(Gdx.files.internal("assets/sprite/rainbow.png"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/bg.wav"));
		bgMusic.setVolume(.1f);
		bgMusic.setLooping(true);
	}
}
