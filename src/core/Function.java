package core;

import com.badlogic.gdx.math.Vector2;

public class Function {
	public float getX(float y) {
		System.err.println("Error in a Function!");
		System.err.println("getX was not overriden, something is wrong.");
		return 0;
	}
	
	public float getY(float x) {
		System.err.println("Error in a Function!");
		System.err.println("getY was not overriden, something is wrong.");
		return 0;
	}
	
	public boolean test(float x, float y) {
		System.err.println("Error in a Function!");
		System.err.println("test(float x, float y) was not overriden, something is wrong.");
		return false;
	}
	
	public boolean test(Vector2 point) {
		return test(point.x, point.y);
	}
}
