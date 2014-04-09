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
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Game implements ApplicationListener, Tickable {
	
	public static Vector2 screenSize, startPosition, center;
	public static Game activeGame;
	public static Random generator = new Random();
	public static FPSLogger logger = new FPSLogger();
	
	public static Preferences prefs;
	public Timer clock;
	
	public RainbowHippie hippie;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public boolean isPaused = false, deathMenuCreated = false;
	public boolean shouldPlayIntro = true, shouldLoadDuringIntro = true, introIsFinished = false;
	public boolean drawInputs = false;
	
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
		cfg.width = 1200;
		cfg.height = 700;
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
		screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		prefs = Gdx.app.getPreferences("rh.prefs");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (int) screenSize.x, (int) screenSize.y);
		batch = new SpriteBatch();
		
		startPosition = new Vector2((Game.screenSize.x/2)-200,(Game.screenSize.y/2)-200);
		center = new Vector2((Game.screenSize.x/2),(Game.screenSize.y/2));

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
		if (shouldPlayIntro) {
			double a = getTime();
			final Texture[] textures = new Texture[133];
			for (int i = 0; i < 132; i ++) {
				try {
					textures[i] = (new Texture(Gdx.files.internal("assets/intro/Frame ("+i+").jpg")));
					//RainbowRay.loadAStep();
				} catch (GdxRuntimeException e) { }
			}
			System.out.println("Time to load rainbow intro textures : " + ((getTime()-a)/1000) + " seconds.");
			final Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/intro/audio/jingle.mp3"));
			music.setVolume(.1f);
			music.setLooping(false);
			music.play();
			Renderable r = new Renderable() {
				double lastTime = getTime();
				int iteration = 0;
				int frame = 0;
				Texture activeFrame = null;
				@Override
				public void render() {
					// If we are not done playing the intro, or the user has not clicked to skip
					if (frame < 133 && !Gdx.input.isTouched()) {
						// Cap the frame rate
						if (getTime()-lastTime > 16.25) {
							// If the next frame exists, use that. If not, it was removed because of its similarity
							//		to the prior frame, this helps bring down download size
							try {
								Texture newFrame = textures[frame+1];
								if (newFrame != null) {
									if (activeFrame != null)
										activeFrame.dispose();
									activeFrame = newFrame;
								}
							}
							catch (IndexOutOfBoundsException e) { }
							catch (NullPointerException e) { }
							frame ++;
						}
						
						//if (iteration % 50 == 0 && shouldLoadDuringIntro)
						//	RainbowRay.loadAStep();
						
						batch.draw(activeFrame, 0, 0, screenSize.x, screenSize.y);
					} else {
						// Only cut the intro music off abruptly if the user clicked to skip
						if (Gdx.input.isTouched()) {
							music.stop();
							music.dispose();
						}
						introIsFinished = true;
						Game.activeGame.toBeRendered.remove(this);
						createMenu();
					}
					iteration ++;
					lastTime = getTime();
				}
			};
			toBeRendered.add(r);
		} else {
			introIsFinished = true;
			createMenu();
		}
	}
	
	@Override
	public void dispose() {
		toBeTicked.clear();
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		Background.renderBackground();
		
		for (int i = 0; i <= toBeRendered.size() - 1; i++)
			toBeRendered.get(i).render();
		
		if (introIsFinished)
			Background.renderForeground();
		
		if (drawInputs == true)
			drawInputs();
		
		batch.end();
	}
	
	private void drawInputs() {
		if (Gdx.input.isTouched(0))
			batch.draw(AssetManager.greenCircle, Gdx.input.getX(0)-(AssetManager.greenCircle.getWidth()/2), screenSize.y-Gdx.input.getY(0)-(AssetManager.greenCircle.getHeight()/2));batch.draw(AssetManager.greenCircle, Gdx.input.getX(0)-(AssetManager.greenCircle.getWidth()/2), screenSize.y-Gdx.input.getY(0)-(AssetManager.greenCircle.getHeight()/2));
		
		if (Gdx.input.isTouched(1))
			batch.draw(AssetManager.greenCircle, Gdx.input.getX(1)-(AssetManager.greenCircle.getWidth()/2), screenSize.y-Gdx.input.getY(1)-(AssetManager.greenCircle.getHeight()/2));batch.draw(AssetManager.greenCircle, Gdx.input.getX(1)-(AssetManager.greenCircle.getWidth()/2), screenSize.y-Gdx.input.getY(1)-(AssetManager.greenCircle.getHeight()/2));
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
	
	public static double getTime() {
		return System.nanoTime()*1E-6;
	}
}