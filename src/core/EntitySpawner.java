package core;

import objects.Balloon;
import objects.Barrel;

import com.badlogic.gdx.graphics.Color;

public class EntitySpawner implements Tickable {
	private static int hippieDistanceFromBottom = 30, hippieDistanceFromTop = 200;
	public static final int SAFE_LOW = 0, SAFE_MIDDLE = 1, SAFE_HIGH = 2;
	
	// Closer to 0 yeilds more balloons, closer to 1 yeilds more barrels
	public static final float balloonVsBarrelSpawnChance = .65f;
	
	public int safeArea;
	public int safeLocationChangeTime = 200;
	public int spawnRate = 50;
	
	private int tickCount = 1;
	private boolean hasIncreasedDifficulty;
	
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
		
		checkForDifficultyIncrease();
		
		if (tickCount % spawnRate == 0 && !RainbowHippie.activeHippie.isDead) {
			if (Game.generator.nextFloat() < balloonVsBarrelSpawnChance)
				spawnEnemy();
			else
				spawnBalloon();
		}
		
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
			System.err.println("Error : cannot set safe area to somthing other than 0, 1 or 2");
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
			if (!hasIncreasedDifficulty) {
				safeLocationChangeTime *= .85;
				spawnRate *= .8;
				hasIncreasedDifficulty = true;
				System.out.println("Increased difficulty!");
				System.out.println("   spawnRate : " + spawnRate);
				System.out.println("   safeLocationChangeTime : " + safeLocationChangeTime);
			}
		} else {
			hasIncreasedDifficulty = false;
		}
	}
	
	public void changeSafeLocation() {
		safeArea = Game.generator.nextInt(3);
		if (safeArea == SAFE_LOW)
			System.out.println("Changed safe area to low");
		else if (safeArea == SAFE_MIDDLE)
			System.out.println("Changed safe area to middle");
		else if (safeArea == SAFE_HIGH)
			System.out.println("Changed safe area to high");
	}
	
	private Color getRandColor() {
		int color = Game.generator.nextInt(5);
		switch(color){
		case 0:
			return Color.BLACK;
		case 1:
			return Color.WHITE;
		case 2:
			return Color.RED;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.GREEN;
		}
		return Color.WHITE;
	}
}