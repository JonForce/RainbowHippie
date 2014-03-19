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
	
	private static Texture lastGenerated;
	private static Vector2 lastLocation = new Vector2(0,0);
	private static float lastCurve = 1;
	
	public static int timesRendered = 0, timesGenerated = 0;
	
	public static void load() {
		data = AssetManager.rainbow.getTextureData();
		mapA = new Pixmap(data.getWidth(), data.getHeight(), data.getFormat());
		mapB = new Pixmap((int)(Game.screenSize.x), (int)(Game.screenSize.y), Format.RGBA8888);
		data.prepare();
		mapA = data.consumePixmap();
		lastGenerated = generateCurvedRainbow(1);
	}
	
	public static void render(float curve, Vector2 location) {
		float deltaCurve = Math.abs(lastCurve-curve);
		
		if (deltaCurve > .000041) {
			lastGenerated = generateCurvedRainbow(curve);
			timesGenerated ++;
			mapB.dispose();
		}
		timesRendered ++;
		Game.activeGame.batch.draw(lastGenerated, location.x, location.y);
		lastLocation = location;
		lastCurve = curve;
	}
	
	public static boolean isPopping(Balloon balloon) {
		//Points on the balloon to be tested for collision
		Vector2[] testPoints = new Vector2[] {
				//Center of the balloon
				new Vector2(balloon.location.x+(AssetManager.balloon.getWidth()/2), balloon.location.y+(AssetManager.balloon.getHeight()/2)-150),
		};
		
		float curve = Game.activeGame.hippie.rainbowBendModifier;
		
		for (Vector2 vector : testPoints) {
			float correctY = (float) ((Math.pow(vector.x, 2)*-curve)+150);
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