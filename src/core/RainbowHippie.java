package core;

import objects.FlyingBodyPart;
import gui.GuiImage;
import gui.StartSign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RainbowHippie implements Renderable, Tickable {
	
	public static RainbowHippie activeHippie;
	
	// Enumerator
	public static final int FLYING = 0;
	public static final int HIDDEN = 1;
	public static final int HOLDING = 2;
	
	// Other immutables
	public final Vector2 hippieSize = new Vector2(AssetManager.fly.getWidth() / 7, AssetManager.fly.getHeight());
	public final int delayAfterDeath = 50;
	
	// Rendering
	public Texture activeTexture;
	public int srcX, srcY, srcWidth, srcHeight, frame = 0;
	public int state = HOLDING;
	public boolean isDead = false, isRainbowing = false;
	public StartSign sign = new StartSign();
	public GuiImage logo = new GuiImage(AssetManager.logo, new Vector2(Game.center.x - (AssetManager.logo.getWidth() / 2), Game.center.y));
	
	// Positional and movement
	public Vector2 location, momentum = new Vector2(0, 0);
	public float damping = .15f;
	public float rainbowBendModifier = 0;
	public boolean lockedX = false, lockedY = false;
	public float airResistance = 15f;
	public float jumpAmount = 2f;
	public float gravity = -1f;
	
	// Collision
	public Rectangle boundingBox;
	
	public RainbowHippie() {
		activeHippie = this;
		location = Game.startPosition;
		activeTexture = AssetManager.fly;
		Game.activeGame.toBeRendered.add(this);
		Game.activeGame.toBeTicked.add(this);
		boundingBox = new Rectangle(0, 0, hippieSize.x - 30, hippieSize.y - 30);
	}
	
	@Override
	public void render() {
		if (state != HIDDEN) {
			Game.activeGame.batch.draw(activeTexture, location.x, location.y, srcX, srcY, srcWidth, srcHeight);
			if (isRainbowing) {
				//130 is the amount from the location.x to where we want to start the rainbow
				//150 is the amount from the location.y to where we want to start the rainbow
				RainbowRay.render(rainbowBendModifier, new Vector2(location.x + 130, (location.y - (Game.screenSize.y / 2)) + 150));
			}
		}
	}
	
	@Override
	public void tick() {
		// Move bounding box to the center of our hippie
		boundingBox.setCenter(new Vector2(location.x + hippieSize.x / 2, location.y + hippieSize.y / 2 + 10));
		
		isRainbowing = Gdx.input.isButtonPressed(Buttons.RIGHT);
		
		// Update based on state
		if (state == FLYING) {
			// Animate
			animate(7, AssetManager.fly);
			sign.visible = false;
			
			if (Gdx.input.isButtonPressed(Buttons.LEFT))
				momentum.y += jumpAmount;
			
			momentum.y += gravity;
			
			// Cap the possible downward momentum
			if (momentum.y < -7.5)
				momentum.y = -7.5f;
			
			// Deltas are the desired position-actualPosition
			float deltaX = 50 - location.x;
			
			// Amount to moves are the deltas*damping
			float amountToMoveY = momentum.y * damping * airResistance;
			float amountToMoveX = deltaX * (damping / 4);
			
			// Move by the amount to move
			if (!lockedX)
				location.x += amountToMoveX;
			if (!lockedY) {
				location.y += amountToMoveY;
				rainbowBendModifier = (amountToMoveY / 50) / 1000;
			}
		} else if (state == HOLDING) {
			animate(7, AssetManager.flyHold);
			sign.useFrame(frame);
			sign.visible = true;
			
			// Assert that hippie may not move, and that he is at the start
			// position
			lockedX = true;
			lockedY = true;
			location = Game.startPosition;
		}
		
		// Assert that our hippie does not leave the playing area
		assertOnScreen();
	}

	public void die() {
		if (!isDead) {
			new FlyingBodyPart(new Vector2(location.x, location.y + 50), FlyingBodyPart.HEAD).velocity = new Vector2(-5, 10);
			new FlyingBodyPart(new Vector2(location.x, location.y + 10), FlyingBodyPart.BODY).torque = 5f;
			new FlyingBodyPart(new Vector2(location.x + 20, location.y + 10), FlyingBodyPart.ARM).velocity = new Vector2(-10, 5);
			new FlyingBodyPart(new Vector2(location.x + 5, location.y + 10), FlyingBodyPart.ARM).velocity = new Vector2(-6, 3);
			new FlyingBodyPart(new Vector2(location.x + 5, location.y), FlyingBodyPart.LEG).velocity = new Vector2(-10, -5);
			new FlyingBodyPart(new Vector2(location.x + 15, location.y), FlyingBodyPart.LEG).velocity = new Vector2(-10, -5);
			state = HIDDEN;
			frame = 0;
			isDead = true;
			Game.activeGame.clearEntitys();
		}
	}
	
	/*
	 * Sets the activeSpritesheet, srcX, width and height.
	 */
	private void animate(int frames, Texture spriteSheet) {
		if (frame >= frames - 1)
			frame = 0;
		else
			frame++;
		
		if (!isRainbowing)
			activeTexture = spriteSheet;
		else
			activeTexture = AssetManager.flyRainbowing;
		srcX = frame * (activeTexture.getWidth() / frames);
		srcY = 0;
		srcWidth = (activeTexture.getWidth() / frames);
		srcHeight = activeTexture.getHeight();
	}
	
	/*
	 * Creates an upper and lower bounds of where the hippie is, by checking if
	 * he is above the top or below the bottom. Called every frame.
	 */
	public void assertOnScreen() {
		if (location.y >= (int) Game.screenSize.y - 199) {
			location.y = (int) Game.screenSize.y - 200;
			momentum.y = 0;
			lockedY = true;
			rainbowBendModifier /= 2;
		} else if (location.y <= 29) {
			location.y = 30;
			momentum.y = 0;
			lockedY = true;
			rainbowBendModifier /= 2;
		} else {
			lockedY = false;
		}
	}
	
	public void reset() {
		activeHippie.state = FLYING;
		location = new Vector2((Game.screenSize.x / 2) - 200, (Game.screenSize.y / 2) - 200);
		isRainbowing = false;
		isDead = false;
	}
}