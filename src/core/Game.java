package core;

import gui.DeathMenu;
import gui.Instructions;
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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game implements ApplicationListener, Tickable {
	
	public static Vector2 screenSize = new Vector2(1200, 700);
	public static final Vector2 startPosition = new Vector2((Game.screenSize.x/2)-200,(Game.screenSize.y/2)-200);
	public static final Vector2 center = new Vector2((Game.screenSize.x/2),(Game.screenSize.y/2));
	public static Game activeGame;
	public static Random generator = new Random();
	public static FPSLogger logger = new FPSLogger();
	
	public static Preferences prefs;
	public Timer clock;
	
	public RainbowHippie hippie;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public boolean isPaused = false, deathMenuCreated = false;
	public boolean shouldPlayIntro = true, shouldLoadDuringIntro = true;
	
	public ArrayList<Renderable> toBeRendered;
	public ArrayList<Tickable> toBeTicked;
	public ArrayList<Tickable> pausedTicked;
	public int tickInterval = 45;
	
	public PauseSign pauseSign;
	public QuitButton quitButton;
	public ScoreCounter scoreCounter;
	public EntitySpawner entitySpawner;
	
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
		entitySpawner = new EntitySpawner();
		Background.startMovingClouds();
		AssetManager.bgMusic.play();
		
		toBeTicked.add(this);
	}
	
	public void restart() {
		Game.activeGame.toBeRendered.add(scoreCounter);
		scoreCounter.reset();
		RainbowHippie.activeHippie.reset();
		deathMenuCreated = false;
		entitySpawner.reset();
		clearEntitys();
	}
	
	public void clearEntitys() {
		for(int i = 0; i <= toBeTicked.size() - 1; i++) {
			Tickable t = toBeTicked.get(i);
			if(t instanceof Balloon || t instanceof Barrel || t instanceof Dolphin) {
				toBeTicked.remove(t);
			}
		}
		for(int i = 0; i <= toBeRendered.size() - 1; i++) {
			Renderable r = toBeRendered.get(i);
			if(r instanceof Balloon || r instanceof Barrel || r instanceof Dolphin) {
				toBeRendered.remove(r);
			}
		}
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
	
	public void createMenu() {
		if (prefs.getBoolean("isFirstLaunch")) {
			Background.load();
			new Instructions();
		} else {
			Background.load();
			hippie = new RainbowHippie();
		}
	}
	
	@Override
	public void create() {
		// Initialization, same for all platforms
		activeGame = this;
		prefs = Gdx.app.getPreferences("rh.prefs");
		screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (int) screenSize.x, (int) screenSize.y);
		batch = new SpriteBatch();

		toBeRendered = new ArrayList<Renderable>();
		toBeTicked = new ArrayList<Tickable>();
		pausedTicked = new ArrayList<Tickable>();
		
		GLTexture.setEnforcePotImages(false);
		AssetManager.loadAssets();
		
		//prefs.putBoolean("isFirstLaunch", true);
		//prefs.flush();
		
		// Start ticking thread
		clock = new Timer();
		clock.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (toBeTicked.size() > 40)
					System.out.println("Warning! There are " + toBeTicked.size() + " tickables! There is probably somthing wrong.");
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
		}, 0, tickInterval);
		
		//Interpolate the playing of the intro and the loading of the RainbowRay
		RainbowRay.initialize();
		Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/intro/audio/jingle.mp3"));
		music.setVolume(.1f);
		music.setLooping(false);
		music.play();
		if (shouldPlayIntro) {
			Renderable r = new Renderable() {
				int i = 0;
				Texture activeFrame = null;
				@Override
				public void render() {
					if (i < 133 && !Gdx.input.isTouched()) {
						if (Gdx.files.internal("assets/intro/Frame ("+(i+1)+").jpg").exists())
							activeFrame = new Texture(Gdx.files.internal("assets/intro/Frame ("+(i+1)+").jpg"));
						
						batch.draw(activeFrame, 0, 0, activeFrame.getWidth()*1.25f, activeFrame.getHeight()*1.3f);
						
						// Generate a texture every 15 frames during intro, until the intro is complete
						//		then generate the rest of them when needed
						if (i % 15 == 0 && shouldLoadDuringIntro)
							RainbowRay.loadAStep();
					} else {
						Game.activeGame.toBeRendered.remove(this);
						createMenu();
					}
					i ++;
				}
			};
			toBeRendered.add(r);
		} else {
			createMenu();
		}
	}
	
	@Override
	public void dispose() {
		
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (int i = 0; i <= toBeRendered.size() - 1; i++) {
			toBeRendered.get(i).render();
		}
		
		batch.end();
	}
	
	@Override
	public void resize(int x, int y) {
		screenSize = new Vector2(x, y);
	}
	
	@Override
	public void tick() {
		// Occasionally spawn a dolphin
		if (generator.nextInt(300) == 0) {
			new Dolphin();
		}
		
		if(hippie.isDead && !deathMenuCreated) {
			Game.activeGame.toBeRendered.remove(scoreCounter);
			new DeathMenu();
			deathMenuCreated = true;
		}
	}
}