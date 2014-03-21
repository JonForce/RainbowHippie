package core;

import objects.Balloon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;

public class RainbowRay {
	
	public static Pixmap mapA, mapB;
	public static TextureData data;
	
	public static Texture[] generatedRainbows;
	private static final float curveInterval = .00003f;
	
	public static void load() {
		data = AssetManager.rainbow.getTextureData();
		mapA = new Pixmap(data.getWidth(), data.getHeight(), data.getFormat());
		mapB = new Pixmap((int)(Game.screenSize.x), (int)(Game.screenSize.y), Format.RGBA8888);
		data.prepare();
		mapA = data.consumePixmap();
		
		generatedRainbows = new Texture[60];
		for (int i = 0; i != generatedRainbows.length; i ++) {
			generatedRainbows[i] = generateCurvedRainbow(i*curveInterval);
		}
	}
	
	public static void render(float curve, Vector2 location) {
		int index = (int) (curve/curveInterval);
		if (index < 0)
			Game.activeGame.batch.draw(generatedRainbows[-index], location.x, location.y-AssetManager.rainbow.getHeight(), location.x, location.y, generatedRainbows[-index].getWidth(), generatedRainbows[-index].getHeight(), 1, 1, 0, 0, 0, generatedRainbows[-index].getWidth(), generatedRainbows[-index].getHeight(), false, true);
		else
			Game.activeGame.batch.draw(generatedRainbows[index], location.x, location.y);
	}
	
	public static boolean isPopping(Balloon balloon) {
		//Points on the balloon to be tested for collision
		Vector2[] testPoints = new Vector2[] {
				//Center of the balloon
				new Vector2(balloon.location.x+(AssetManager.balloon.getWidth()/2), balloon.location.y+(AssetManager.balloon.getHeight()/2)+50),
				//Top of the balloon
				new Vector2(balloon.location.x+(AssetManager.balloon.getWidth()/2), balloon.location.y+(AssetManager.balloon.getHeight()/2)+100),
				//Bottom of the balloon
				new Vector2(balloon.location.x+(AssetManager.balloon.getWidth()/2), balloon.location.y+(AssetManager.balloon.getHeight()/2)+10)
		};
		
		float curve = Game.activeGame.hippie.rainbowBendModifier;
		
		for (Vector2 vector : testPoints) {
			float correctY = (float) ((Math.pow(vector.x-130, 2)*-curve) + RainbowHippie.activeHippie.location.y + AssetManager.rainbow.getHeight());
			if (vector.y >= correctY && vector.y <= correctY+AssetManager.rainbow.getHeight()) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Texture generateCurvedRainbow(float curve) {
		mapB = new Pixmap((int)(Game.screenSize.x), (int)(Game.screenSize.y), Format.RGBA8888);
		for (int y = 0; y != mapA.getHeight(); y ++) {
			for (int x = 0; x != mapA.getWidth(); x ++) {
				float deltaY = (float) (Math.pow(x, 2)*curve);
				mapB.drawPixel(x,(int) (y+(mapB.getHeight()/2)+deltaY),mapA.getPixel(x,y));
			}
		}
		return new Texture(mapB);
	}
}