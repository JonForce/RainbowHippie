package core;

import gui.ScoreCounter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import objects.Barrel;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import aesthetics.Background;
import aesthetics.Partical;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game implements ApplicationListener, Tickable {
	
	public static final Vector2 screenSize = new Vector2(1200, 700);
	public static final Vector2 startPosition = new Vector2((Game.screenSize.x/2)-200,(Game.screenSize.y/2)-200);
	public static final Vector2 center = new Vector2((Game.screenSize.x/2),(Game.screenSize.y/2));
	public static Game activeGame;
	
	public static Random generator = new Random();
	
	private Timer clock;
	private int tickCount = 0;
	
	public RainbowHippie hippie;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public ArrayList<Renderable> toBeRendered;
	public ArrayList<Tickable> toBeTicked;
	
	public static void main(String[] args) {
		//Use the desktop configuration
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Rainbow Hippie";
		cfg.width = (int)screenSize.x;
		cfg.height = (int)screenSize.y;
		cfg.resizable = false;
		cfg.addIcon("assets/website_icon.png", FileType.Internal);
		cfg.addIcon("assets/taskbar_icon.png", FileType.Internal);
		cfg.addIcon("assets/window_icon.png", FileType.Internal);
		new LwjglApplication(new Game(), cfg);
	}
	
	public void startGame() {
		hippie.state = RainbowHippie.FLYING;
		hippie.lockedX = false;
		hippie.lockedY = false;
		hippie.sign.disable();
		hippie.logo.fadeAway(.1f);
		Background.startMovingClouds();
	}
	
	@Override
	public void create() {
		//Initialization, same for all platforms
		activeGame = this;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (int)screenSize.x, (int)screenSize.y);
		batch = new SpriteBatch();
		
		toBeRendered = new ArrayList<Renderable>();
		toBeTicked = new ArrayList<Tickable>();
		
		Texture.setEnforcePotImages(false);
		AssetManager.loadAssets();
		Background.load();
		toBeTicked.add(this);
		
		//Start animation thread
    	clock = new Timer();
    	clock.scheduleAtFixedRate(new TimerTask() {
    		  @Override
    		  public void run() {
    			  for (int i = 0; i <= toBeTicked.size()-1; i ++) {
    				  toBeTicked.get(i).tick();
    			  }
    		  }
    	}, 0, 50);
		
    	//Create our hippie
		hippie = new RainbowHippie();
		
		//********************TESTING**********************
		
	}
	
	@Override
	public void dispose() {
		clock.cancel();
	}
	
	@Override
	public void pause() {
		
	}
	
	@Override
	public void resume() {
		
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (int i = 0; i <= toBeRendered.size()-1; i ++) {
			toBeRendered.get(i).render();
		}
		
		batch.end();
	}
	
	@Override
	public void resize(int x, int y) {	}

	@Override
	public void tick() {
		tickCount ++;
		if (generator.nextInt((999-ScoreCounter.activeCounter.score())/10) == 0) {
			new Barrel(Barrel.randomInt(50, (int)screenSize.y-50));
		}
	}
}
