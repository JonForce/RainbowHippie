package core;

import gui.PauseMenu;
import gui.PauseSign;
import gui.QuitButton;
import gui.ScoreCounter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import objects.Barrel;
import objects.FlyingBodyPart;
import aesthetics.Background;
import aesthetics.RecursiveImage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game implements ApplicationListener, Tickable {
	
	public static boolean hasFinishedIntro = true;
	public static final Vector2 screenSize = new Vector2(1200, 700);
	public static final Vector2 startPosition = new Vector2((Game.screenSize.x/2)-200,(Game.screenSize.y/2)-200);
	public static final Vector2 center = new Vector2((Game.screenSize.x/2),(Game.screenSize.y/2));
	public static Game activeGame;
	public static Random generator = new Random();
	public static FPSLogger logger = new FPSLogger();
	
	private Timer clock;
	private int tickCount = 0;
	
	public RainbowHippie hippie;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public boolean isPaused = false;
	
	public ArrayList<Renderable> toBeRendered;
	public ArrayList<Tickable> toBeTicked;
	public ArrayList<Tickable> pausedTicked;
	
	public PauseSign pauseSign;
	public QuitButton quitButton;
	public ScoreCounter scoreCounter;
	
	public static void main(String[] args) {
		// Use the desktop configuration
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
	
	public void start() {
		hippie.state = RainbowHippie.FLYING;
		hippie.lockedX = false;
		hippie.lockedY = false;
		hippie.sign.disable();
		hippie.logo.fadeAway(.1f);
		pauseSign = new PauseSign();
		quitButton = new QuitButton();
		scoreCounter = new ScoreCounter();
		Background.startMovingClouds();
		AssetManager.bgMusic.play();
		
		toBeTicked.add(this);
	}
	
	public void restart() {
		scoreCounter.reset();
	}
	
	public void quit() {
		Gdx.app.exit();
	}
	
	@Override
	public void pause() {
		isPaused = true;
		AssetManager.bgMusic.pause();
	}
	
	@Override
	public void resume() {
		isPaused = false;
		AssetManager.bgMusic.play();
	}
	
	@Override
	public void create() {
		// Initialization, same for all platforms
		activeGame = this;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (int) screenSize.x, (int) screenSize.y);
		batch = new SpriteBatch();

		toBeRendered = new ArrayList<Renderable>();
		toBeTicked = new ArrayList<Tickable>();
		pausedTicked = new ArrayList<Tickable>();

		Texture.setEnforcePotImages(false);
		AssetManager.loadAssets();
		Background.load();
		
		// Start animation thread
		clock = new Timer();
		clock.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i <= toBeTicked.size() - 1; i++) {
					Tickable t = toBeTicked.get(i);
					if(pauseSign != null) {
						if(isPaused) { 
							if(pausedTicked.contains(t)){
								t.tick();
							}
						} else {
							t.tick();
						}
					} else {
						t.tick();
					}
				}
			}
		}, 0, 50);

		// Create our hippie
		hippie = new RainbowHippie();
		
		//**********************
		//RainbowRay.load();
	}

	@Override
	public void dispose() {
		clock.cancel();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		logger.log();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (int i = 0; i <= toBeRendered.size() - 1; i++) {
			toBeRendered.get(i).render();
		}
		//RainbowRay.render(1);
		batch.end();
	}

	@Override
	public void resize(int x, int y) {
	}

	@Override
	public void tick() {
		tickCount++;
		//The barrel spawning needs work, its pretty terrible right now
		if (generator.nextInt((999 - scoreCounter.score()) / 10) == 0 && !hippie.isDead) {
			new Barrel(Barrel.randomInt(50, (int) screenSize.y - 50));
		}
	}
}
