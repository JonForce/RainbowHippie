package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
	
	//Hippie junk
	public static Texture fly, flyHold, flyRainbowing;
	public static Texture head, body, arm, leg, openMouth;
	
	//GUI
	public static Texture sign, logo;
	public static Texture tip1, tip2, tip3;
	public static Texture pause, pauseWindow, resumeButton, restartButton, quitButton, quitCloud, deathWindow, playAgain, facebook, twitter;
	public static Texture font;
	
	//Background images
	public static Texture bg, mountain;
	public static Texture cloudA, cloudB, cloudC;
	
	//Objects
	public static Texture barrel, balloon;
	public static Texture dolphin, whale;
	
	//Effects
	public static Texture gasTrail, rainbow;
	public static Texture plusOne, minusTwo;
	public static Texture redCircle, greenCircle;
	public static Music bgMusic;
	public static Sound introSong;
	
	public static void loadAssets() {
		fly = new Texture(Gdx.files.internal("assets/sprite/fly.png"));
		flyHold = new Texture(Gdx.files.internal("assets/sprite/flyhold.png"));
		flyRainbowing = new Texture(Gdx.files.internal("assets/sprite/rainbowfly.png"));
		body = new Texture(Gdx.files.internal("assets/sprite/body.png"));
		head = new Texture(Gdx.files.internal("assets/sprite/head.png"));
		arm = new Texture(Gdx.files.internal("assets/sprite/arm.png"));
		leg = new Texture(Gdx.files.internal("assets/sprite/leg.png"));
		openMouth  = new Texture(Gdx.files.internal("assets/sprite/mouth.png"));
		
		tip1 = new Texture(Gdx.files.internal("assets/ui/tip1.png"));
		tip2 = new Texture(Gdx.files.internal("assets/ui/tip2.png"));
		tip3 = new Texture(Gdx.files.internal("assets/ui/tip3.png"));
		sign = new Texture(Gdx.files.internal("assets/sprite/signfloat.png"));
		logo = new Texture(Gdx.files.internal("assets/logo.png"));
		font = new Texture(Gdx.files.internal("assets/sprite/font.png"));
		pause = new Texture(Gdx.files.internal("assets/ui/pause.png"));
		pauseWindow = new Texture(Gdx.files.internal("assets/ui/pausewindow.png"));
		resumeButton = new Texture(Gdx.files.internal("assets/ui/resume.png"));
		restartButton = new Texture(Gdx.files.internal("assets/ui/restart.png"));
		quitButton = new Texture(Gdx.files.internal("assets/ui/rage quit.png"));
		deathWindow = new Texture(Gdx.files.internal("assets/ui/window.png"));
		playAgain = new Texture(Gdx.files.internal("assets/ui/playAgain.png"));
		quitCloud = new Texture(Gdx.files.internal("assets/ui/ragequit.png"));
		facebook = new Texture(Gdx.files.internal("assets/ui/facebook.png"));
		twitter = new Texture(Gdx.files.internal("assets/ui/twitter.png"));
		
		bg = new Texture(Gdx.files.internal("assets/bg/bg.png"));
		mountain = new Texture(Gdx.files.internal("assets/bg/mountain.png"));
		cloudA = new Texture(Gdx.files.internal("assets/bg/cloud00.png"));
		cloudB = new Texture(Gdx.files.internal("assets/bg/cloud01.png"));
		cloudC = new Texture(Gdx.files.internal("assets/bg/cloud02.png"));
		
		barrel = new Texture(Gdx.files.internal("assets/sprite/barrel.png"));
		balloon = new Texture(Gdx.files.internal("assets/sprite/balloon.png"));
		dolphin = new Texture(Gdx.files.internal("assets/sprite/dolphin.png"));
		whale = new Texture(Gdx.files.internal("assets/sprite/whale.png"));
		
		redCircle = new Texture(Gdx.files.internal("assets/debugging/redCircle.png"));
		greenCircle = new Texture(Gdx.files.internal("assets/debugging/greenCircle.png"));
		gasTrail = new Texture(Gdx.files.internal("assets/sprite/Gas_Trail.png"));
		rainbow = new Texture(Gdx.files.internal("assets/sprite/rainbow.png"));
		plusOne = new Texture(Gdx.files.internal("assets/ui/+1.png"));
		minusTwo = new Texture(Gdx.files.internal("assets/ui/-2.png"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/bg.wav"));
		bgMusic.setVolume(.1f);
		bgMusic.setLooping(true);
	}
}