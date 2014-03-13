package core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

public class RainbowRay {
	
	/*
	public static TextureData data;
	public static Mesh mesh;
	
	public static void load() {
		data = AssetManager.rainbow.getTextureData();
		VertexAttribute[] attributes = new VertexAttribute[10];
		attributes[0] = new VertexAttribute(VertexAttributes.Usage.Color, 1, "color1");
		mesh = new Mesh(false, data.getWidth()*data.getHeight(), 0, new VertexAttributes(attributes));
	}
	
	public static void render() {
		
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	public static Pixmap mapA, mapB;
	public static TextureData data;
	
	public static void load() {
		data = AssetManager.rainbow.getTextureData();
		mapA = new Pixmap(data.getWidth(), data.getHeight(), data.getFormat());
		mapB = new Pixmap((int)(Game.screenSize.x), (int)(Game.screenSize.y), Format.RGBA8888);
		data.prepare();
		mapA = data.consumePixmap();
	}
	
	public static void render(float curve) {
		//mapB = new Pixmap((int)(Game.screenSize.x), (int)(Game.screenSize.y), Format.RGBA8888);
		for (int y = 0; y != mapA.getHeight(); y ++) {
			for (int x = 0; x != mapA.getWidth(); x ++) {
				//mapB.drawPixel(x, y, 0);
				mapB.drawPixel(x,y,mapA.getPixel(x,y));
			}
		}
		Game.activeGame.batch.draw(new Texture(mapB), 0, -100);
		//==mapB.dispose();
	}
	*/
}