package core;

import objects.Balloon;
import objects.Barrel;

import com.badlogic.gdx.graphics.Color;

public class EntitySpawner implements Tickable {
	private static int hippieDistanceFromBottom = 30, hippieDistanceFromTop = 200;
	public static final int SAFE_LOW = 0, SAFE_MIDDLE = 1, SAFE_HIGH = 2;
	
	// Closer to 0 yeilds more balloons, closer to 1 yeilds more barrels
	public static float balloonVsBarrelSpawnChance = .5f;
	
	// Closer to 0 yeilds less
	public static float blackBalloonSpawnChance = .1f;
	
	// The minimum safe location change rate
	public static int safeLocationChangeTimeCap = 125;
	
	public int safeArea;
	public int safeLocationChangeTime = 200;
	public int spawnRate = 35;
	
	private int tickCount = 1;
	private boolean hasChangedDifficulty;
	private int previousScore = 1;
	
	public EntitySpawner() {
		safeArea = SAFE_LOW;
		changeSafeLocation();
		Game.activeGame.toBeTicked.add(this);
	}
	
	public void reset() {
		safeLocationChangeTime = 200;
		spawnRate = 50;
	}

	@Override
	public void tick() {
		if (tickCount % safeLocationChangeTime == 0)
			changeSafeLocation();
		
		if (Game.activeGame.scoreCounter.score > previousScore)
			checkForDifficultyIncrease();
		else
			checkForDifficultyDecrease();
		
		if (tickCount % spawnRate == 0 && !RainbowHippie.activeHippie.isDead) {
			if (Game.generator.nextFloat() < balloonVsBarrelSpawnChance)
				spawnEnemy();
			else
				spawnBalloon();
		}
		
		previousScore = Game.activeGame.scoreCounter.score;
		tickCount ++;
	}
	private void spawnEnemy() {
		if (safeArea == SAFE_HIGH) {
			new Barrel(hippieDistanceFromBottom+Game.generator.nextInt((int) ((Game.screenSize.y/2)-hippieDistanceFromBottom)));
		} else if (safeArea == SAFE_MIDDLE) {
			if (Game.generator.nextBoolean())
				new Barrel(hippieDistanceFromBottom+Game.generator.nextInt((int) ((Game.screenSize.y/4)-hippieDistanceFromBottom)));
			else
				new Barrel((Game.screenSize.y/1.5f)+Game.generator.nextInt((int) ((Game.screenSize.y/2)-hippieDistanceFromTop)));
		} else if (safeArea == SAFE_LOW) {
			new Barrel((Game.screenSize.y/2)+Game.generator.nextInt((int) ((Game.screenSize.y/2)-hippieDistanceFromTop)));
		} else {
			System.err.println("Error (EntitySpawner) : cannot set safe area to somthing other than 0, 1 or 2");
			System.exit(1);
		}
	}
	
	private void spawnBalloon() {
		if (Game.generator.nextInt(10) == 0) {
			//Spawn event 1 : 10% chance
			Balloon[] balloons = new Balloon[]{
				new Balloon(Color.GREEN),
				new Balloon(Color.RED),
				new Balloon(Color.GREEN),
				new Balloon(Color.RED),
			};
			int i = 1;
			for (Balloon b : balloons) {
				b.verticalAmountModifier = 70;
				b.verticalSpeedModifier = 20+Game.generator.nextInt(20);
				b.location.y = (Game.screenSize.y/2)-200;
				b.isSwerving = true;
				b.moveSpeed = 10-i;
				i += 2;
			}
		} else {
			//Normal spawn : 90% chance
			new Balloon(getRandColor());
		}
	}
	
	private void checkForDifficultyIncrease() {
		if (Game.activeGame.scoreCounter.score % 10 == 0) {
			if (!hasChangedDifficulty) {
				if (safeLocationChangeTime > safeLocationChangeTimeCap)
					safeLocationChangeTime *= .875;
				spawnRate *= .8;
				hasChangedDifficulty = true;
			}
		} else {
			hasChangedDifficulty = false;
		}
	}
	
	private void checkForDifficultyDecrease() {
		if (Game.activeGame.scoreCounter.score % 10 == 0) {
			if (!hasChangedDifficulty) {
				safeLocationChangeTime *= 1.25;
				spawnRate *= 1.2;
				hasChangedDifficulty = true;
			}
		} else {
			hasChangedDifficulty = false;
		}
	}
	
	public void changeSafeLocation() {
		System.out.println("Changing safe location. Change rate : " + safeLocationChangeTime);
		safeArea = Game.generator.nextInt(3);
	}
	
	private Color getRandColor() {
		if (Game.generator.nextFloat() > blackBalloonSpawnChance) {
			int color = Game.generator.nextInt(5);
			switch(color){
				case 0:
					return Color.GREEN;
				case 1:
					return Color.WHITE;
				case 2:
					return Color.RED;
				case 3:
					return Color.BLUE;
				case 4:
					return Color.WHITE;
			}
		} else {
			return Color.BLACK;
		}
		return null;
	}
}