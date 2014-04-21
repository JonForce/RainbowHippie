package gui;

import com.badlogic.gdx.graphics.Texture;

import core.AssetManager;
import core.Game;
import core.Renderable;

public class ScoreCounter implements Renderable {
	
	private Texture font = AssetManager.font;
	public int digitSize;
	public int horizontalMargin = 3;
	public int score = 1;
	
	public ScoreCounter() {
		this(true);
	}
	
	public ScoreCounter(boolean shouldBeRendered) {
		if(shouldBeRendered)
			Game.activeGame.toBeRendered.add(this);
		digitSize = font.getWidth()/10;
	}
	
	@Override
	public void render() {
		render(10, Game.screenSize.y-font.getHeight()-10);
	}
	
	public void render(int number, float x, float y) {
		int oldScore = score;
		score = number;
		render(x, y);
		score = oldScore;
	}
	
	public void render(float x, float y) {
		// Digits (from 0 to 2) are in ascending value
		// Ex : if score = 193 scoreDigits[0]=3 scoreDigits[1]=9 scoreDigits[0]=1
		int[] scoreDigits = new int[3];
		scoreDigits[0] = score % 10;
		scoreDigits[1] = (score % 100)/10;
		scoreDigits[2] = (score % 1000)/100;
		if (scoreDigits[2] != 0) {
			renderDigit(scoreDigits[2], (int) x, (int) y);
			renderDigit(scoreDigits[1], (int) x + digitSize + horizontalMargin, (int) y);
			renderDigit(scoreDigits[0], (int) x + 2*(digitSize + horizontalMargin), (int) y);
		} else if (scoreDigits[1] != 0) {
			renderDigit(scoreDigits[1], (int) x, (int) y);
			renderDigit(scoreDigits[0], (int) x + digitSize + horizontalMargin, (int) y);
		} else {
			renderDigit(scoreDigits[0], (int) x, (int) y);
		}
	}
	
	public void renderDigit(int digit, int x, int y) {
		if (digit == 0)
			Game.activeGame.batch.draw(font, x, y, 9*digitSize, 0, digitSize, font.getHeight());
		else
			Game.activeGame.batch.draw(font, x, y, (digit-1)*digitSize, 0, digitSize, font.getHeight());
	}
	
	public void reset() {
		score = 1;
	}
}