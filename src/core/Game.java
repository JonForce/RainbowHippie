package core;

import gui.DeathMenu;
import gui.PauseSign;
import gui.QuitButton;
import gui.ScoreCounter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import objects.Balloon;
import objects.Barrel;
import aesthetics.Background;
import aesthetics.Dolphin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
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
	
	public static Preferences prefs;
	
	private Timer clock;
	private int tickCount = 0;
	private int minimumSpawnTime = 400;
	
	public RainbowHippie hippie;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public boolean isPaused = false, deathMenuCreated = false;
	
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
		
		prefs = Gdx.app.getPreferences("rh.prefs");
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
		Game.activeGame.toBeRendered.add(scoreCounter);
		scoreCounter.reset();
		RainbowHippie.activeHippie.reset();
		for(int i = 0; i <= toBeTicked.size() - 1; i++) {
			Tickable t = toBeTicked.get(i);
			if(t instanceof Balloon || t instanceof Barrel) {
				toBeTicked.remove(t);
			}
		}
		for(int i = 0; i <= toBeRendered.size() - 1; i++) {
			Renderable r = toBeRendered.get(i);
			if(r instanceof Balloon || r instanceof Barrel) {
				toBeRendered.remove(r);
			}
		}
		deathMenuCreated = false;
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

		GLTexture.setEnforcePotImages(false);
		AssetManager.loadAssets();
		Background.load();
		
		// Start ticking thread
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
		
		RainbowRay.load();
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
		
		batch.end();
	}
	
	@Override
	public void resize(int x, int y) {
	}
	
	private void doEasyEvent() {
		if (generator.nextBoolean()) {
			new Barrel(100);
			new Barrel(170);
			new Balloon(getRandColor()).location.y = 125;
			new Balloon(getRandColor()).location.y = 200;
		} else {
			new Barrel(screenSize.y-100-AssetManager.barrel.getHeight());
			new Barrel(screenSize.y-170-AssetManager.barrel.getHeight());
			new Balloon(getRandColor()).location.y = screenSize.y-125-AssetManager.barrel.getHeight();
			new Balloon(getRandColor()).location.y = screenSize.y-200-AssetManager.barrel.getHeight();
		}
	}
	
	private void doMediumEvent() {
		if (generator.nextBoolean()) {
			new Barrel(100).velocity = 15;
			new Barrel(170).velocity = 15;
			new Barrel(250).velocity = 15;
		} else {
			new Barrel(screenSize.y-100-AssetManager.barrel.getHeight()).velocity = 15;
			new Barrel(screenSize.y-170-AssetManager.barrel.getHeight()).velocity = 15;
			new Barrel(screenSize.y-250-AssetManager.barrel.getHeight()).velocity = 15;
		}
	}
	
	private void randomSpawn() {
		if (generator.nextBoolean()) {
			new Barrel(generator.nextInt((int) (screenSize.y-AssetManager.barrel.getHeight())));
		} else {
			new Balloon(getRandColor());
		}
	}
	
	@Override
	public void tick() {
		tickCount++;
		
		// Occasionally spawn a dolphin
		if (generator.nextInt(300) == 0) {
			new Dolphin();
		}
		
		// Spawning control
		if (!hippie.isDead) {
			if (tickCount % minimumSpawnTime == 0) {
				randomSpawn();
				return;
			}
			
			if (scoreCounter.score < 10) {
				minimumSpawnTime = 50;
				if (generator.nextInt(600) == 0) {
					//Event
					if (generator.nextInt(3) > 1)
						doEasyEvent();
					else
						doMediumEvent();
					return;
				}
			} else if (scoreCounter.score < 20) {
				minimumSpawnTime = 30;
				if (generator.nextInt(400) == 0) {
					//Event
					if (generator.nextInt(3) > 2)
						doEasyEvent();
					else
						doMediumEvent();
					return;
				}
			} else if (scoreCounter.score < 30) {
				minimumSpawnTime = 20;
				if (generator.nextInt(400) == 0) {
					//Event
					doMediumEvent();
					return;
				}
			} else if (scoreCounter.score < 40) {
				minimumSpawnTime = 15;
				if (generator.nextInt(400) == 0) {
					//Event
					doMediumEvent();
					return;
				}
			}
		}
		
		if(hippie.isDead && !deathMenuCreated) {
			Game.activeGame.toBeRendered.remove(scoreCounter);
			new DeathMenu();
			deathMenuCreated = true;
		}
	}
	
	private Color getRandColor() {
		int color = generator.nextInt(5);
		switch(color){
		case 0:
			return Color.BLACK;
		case 1:
			return Color.WHITE;
		case 2:
			return Color.RED;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.GREEN;
		}
		return Color.WHITE;
	}
}
