package core;

import gui.GuiImage;
import gui.StartSign;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RainbowHippie implements Renderable, Tickable {
	
	public static RainbowHippie activeHippie;
	
	//Enumerator
	public static final int FLYING = 0;
	public static final int HIDDEN = 1;
	public static final int HOLDING = 2;
	
	//Other immutables
	public static final Vector2 hippieSize = new Vector2(AssetManager.fly.getWidth()/7, AssetManager.fly.getHeight());
	
	//Rendering
	public Texture activeTexture;
	public int srcX, srcY, srcWidth, srcHeight;
	public int state = HOLDING;
	private int frame = 0;
	public StartSign sign = new StartSign();
	public GuiImage logo = new GuiImage(AssetManager.logo, new Vector2(Game.center.x-(AssetManager.logo.getWidth()/2), Game.center.y));
	
	//Positional and movement
	public Vector2 location;
	public float damping = .15f;
	public boolean lockedX = false;
	public boolean lockedY = false;
	
	//Collision
	public Rectangle boundingBox;
	
	public RainbowHippie() {
		activeHippie = this;
		location = Game.startPosition;
		activeTexture = AssetManager.fly;
		Game.activeGame.toBeRendered.add(this);
		Game.activeGame.toBeTicked.add(this);
		boundingBox = new Rectangle(0,0,hippieSize.x-30, hippieSize.y-30);
	}
	
	@Override
	public void render() {
		Game.activeGame.batch.draw(activeTexture, location.x, location.y,
									srcX, srcY, srcWidth, srcHeight);
	}
	
	@Override
	public void tick() {
		//Move bounding box to the center of our hippie
		boundingBox.setCenter(new Vector2(location.x+hippieSize.x/2, location.y+hippieSize.y/2));
		
		//Update based on state
		if (state == FLYING) {
			//Animate
			animate(7, AssetManager.fly);
			sign.visible = false;
			
			//Deltas are the desired position-actualPosition
			float deltaY = Mouse.getY()-(location.y+90);
			float deltaX = 150-(location.x+100);
			//Amount to moves are the deltas*daming
			float amountToMoveY = deltaY*damping;
			float amountToMoveX = deltaX*(damping/4);
			//Move by the amount to move
			if (!lockedX)
				location.x += amountToMoveX;
			if (!lockedY)
				location.y += amountToMoveY;
		} else if (state == HOLDING) {
			animate(7, AssetManager.flyHold);
			sign.useFrame(frame);
			sign.visible = true;
			
			//Assert that hippie may not move, and that he is at the start position
			lockedX = true;
			lockedY = true;
			location = Game.startPosition;
		} else if (state == HIDDEN) {
			
		}
		
		//Assert that our hippie does not leave the playing area
		assertOnScreen();
	}
	
	/*
	 * Sets the activeSpritesheet, srcX, width and height. 
	 */
	private void animate(int frames, Texture spriteSheet) {
		if (frame >= frames-1)
			frame = 0;
		else
			frame ++;
		
		activeTexture = spriteSheet;
		srcX = frame*(spriteSheet.getWidth()/frames);
		srcY = 0;
		srcWidth = (spriteSheet.getWidth()/frames);
		srcHeight = spriteSheet.getHeight();
	}
	
	/*
	 * Creates an upper and lower bounds of where the hippie is,
	 * by checking if he is above the top or below the bottom. Called every frame.
	 */
	public void assertOnScreen() {
		if (location.y >= (int)Game.screenSize.y-200) {
			location.y = (int)Game.screenSize.y-200;
		} else if (location.y <= 30) {
			location.y = 30;
		}
	}
}