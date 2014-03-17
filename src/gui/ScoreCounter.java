package gui;

import com.badlogic.gdx.graphics.Texture;

import core.AssetManager;
import core.Game;
import core.Renderable;

public class ScoreCounter implements Renderable {
	
	//Considers first to be smallest valued digit, second second smallest valued digit and so on.
	public int scoreFirstDigit = 1;
	public int scoreSecondDigit = 0;
	public int scoreThirdDigit = 0;
	private Texture font = AssetManager.font;
	
	public ScoreCounter() {
		this(true);
	}
	
	public ScoreCounter(boolean shouldBeRendered) {
		if(shouldBeRendered)
			Game.activeGame.toBeRendered.add(this);
	}
	
	@Override
	public void render() {
		if (scoreThirdDigit == 0 && scoreSecondDigit == 0) {
			if (scoreFirstDigit == 0)
				scoreFirstDigit = 10;
			Game.activeGame.batch.draw(font, 10, Game.screenSize.y-font.getHeight()-10,
					(scoreFirstDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			if (scoreFirstDigit == 10)
				scoreFirstDigit = 0;
		} else if (scoreThirdDigit == 0) {
			if (scoreFirstDigit == 0)
				scoreFirstDigit = 10;
			if (scoreSecondDigit == 0)
				scoreSecondDigit = 10;
			Game.activeGame.batch.draw(font, (font.getWidth()/10)+5, Game.screenSize.y-font.getHeight()-10,
					(scoreFirstDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, 10, Game.screenSize.y-font.getHeight()-10,
					(scoreSecondDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			if (scoreFirstDigit == 10)
				scoreFirstDigit = 0;
			if (scoreSecondDigit == 10)
				scoreSecondDigit = 0;
		} else {
			Game.activeGame.batch.draw(font, (font.getWidth()/10)*2+5, Game.screenSize.y-font.getHeight()-10,
					(scoreFirstDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, (font.getWidth()/10)+5, Game.screenSize.y-font.getHeight()-10,
					(scoreSecondDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, 10, Game.screenSize.y-font.getHeight()-10,
					(scoreThirdDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
		}
	}
	
	public void render(float x, float y) {
		if (scoreThirdDigit == 0 && scoreSecondDigit == 0) {
			if (scoreFirstDigit == 0)
				scoreFirstDigit = 10;
			Game.activeGame.batch.draw(font, x, y - font.getHeight() - 10,
					(scoreFirstDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			if (scoreFirstDigit == 10)
				scoreFirstDigit = 0;
		} else if (scoreThirdDigit == 0) {
			if (scoreFirstDigit == 0)
				scoreFirstDigit = 10;
			if (scoreSecondDigit == 0)
				scoreSecondDigit = 10;
			Game.activeGame.batch.draw(font, (font.getWidth() / 10) + x, y - font.getHeight() - 10,
					(scoreFirstDigit - 1) * (font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, x, y - font.getHeight()-10,
					(scoreSecondDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			if (scoreFirstDigit == 10)
				scoreFirstDigit = 0;
			if (scoreSecondDigit == 10)
				scoreSecondDigit = 0;
		} else {
			Game.activeGame.batch.draw(font, (font.getWidth()/10) + x, y - font.getHeight()-10,
					(scoreFirstDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, (font.getWidth()/10)+x, y-font.getHeight()-10,
					(scoreSecondDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
			Game.activeGame.batch.draw(font, x, y-font.getHeight()-10,
					(scoreThirdDigit-1)*(font.getWidth()/10), 0, (font.getWidth()/10), font.getHeight());
		}
	}
	
	public void reset() {
		scoreFirstDigit = 1;
		scoreSecondDigit = 0;
		scoreThirdDigit = 0;
	}
	
	public int score() {
		return scoreFirstDigit+(scoreSecondDigit*10)+(scoreThirdDigit*100);
	}
	
	public void add(int amount) {
		for (int i = 0; i != amount; i ++) {
			addOne();
		}
	}
	
	public void subtract(int amount) {
		for (int i = 0; i != amount; i ++) {
			subtractOne();
		}
	}
	
	public void addOne() {
		scoreFirstDigit ++;
		if (scoreFirstDigit > 9) {
			scoreFirstDigit = 0;
			scoreSecondDigit ++;
			if (scoreSecondDigit > 9) {
				scoreSecondDigit = 0;
				scoreThirdDigit ++;
			}
		}
	}
	
	public void subtractOne() {
		scoreThirdDigit --;
		if (scoreThirdDigit < 0) {
			scoreThirdDigit = 0;
			scoreSecondDigit --;
			if (scoreSecondDigit < 0) {
				scoreSecondDigit = 0;
				scoreFirstDigit --;
			}
		}
	}
}
