package core;

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
		
		if (deltaCurve > 0) {
			lastGenerated = generateCurvedRainbow(curve);
			timesGenerated ++;
			mapB.dispose();
		}
		timesRendered ++;
		Game.activeGame.batch.draw(lastGenerated, location.x, location.y);
		lastLocation = location;
		lastCurve = curve;
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