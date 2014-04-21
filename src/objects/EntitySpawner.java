package objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import core.Game;
import core.RainbowHippie;
import core.Tickable;

public class EntitySpawner implements Tickable {
	
	// Closer to 0 yeilds less black balloons
	public static float blackBalloonSpawnChance = .1f;
	
	public static int balloonSpawnDelayMinimum = 25;
	
	public int balloonSpawnDelay = 75, obstacleSpawnDelay = 17, initialDelay;
	public boolean paused = false;
	
	private int tickCount = 1, lastBalloonSpawnTime = 1;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private Obstacle activeObstacle;
	private RainbowHippie hippie;
	
	public EntitySpawner() {
		hippie = Game.activeGame.hippie;
		Game.activeGame.toBeTicked.add(this);
		obstacles.add(new Obstacle(-1, 5) {
			@Override
			public void create() {
				super.create();
				int rand = Game.generator.nextInt(3);
				if (rand == 0)
					createBarrel(75, 20 + Game.generator.nextInt(15), 0);
				else if (rand == 1)
					createBarrel(250, 20 + Game.generator.nextInt(15), 0);
				else
					createBarrel(500, 20 + Game.generator.nextInt(15), 0);
			}
		});
		obstacles.add(new Obstacle(0, 5) {
			@Override
			public void create() {
				super.create();
				createBarrel((int) hippie.location.y, 20 + Game.activeGame.scoreCounter.score*3, 0);
				if (hippie.location.y > Game.screenSize.y/2)
					createBarrel(500, 20 + Game.activeGame.scoreCounter.score*4, 0);
				else
					createBarrel(75, 20 + Game.activeGame.scoreCounter.score*4, 0);
			}
		});
		obstacles.add(new Obstacle(4, 10) {
			@Override
			public void create() {
				super.create();
				int speed = 17 + Game.activeGame.scoreCounter.score;
				if (Game.generator.nextBoolean()) {
					createBarrel(75, speed, 20);
					createBarrel(250, speed, 25);
					createBarrel(500, 40, 20);
				} else if (Game.generator.nextBoolean()) {
					createBarrel(75, 40, 20);
					createBarrel(300, speed, 25);
					createBarrel(500, speed, 20);
				} else {
					createBarrel(75, 10, 20);
					createBarrel(300, 40, 25);
					createBarrel(500, 13, 20);
				}
			}
		});
		obstacles.add(new Obstacle(10, 25) {
			@Override
			public void create() {
				super.create();
				int speed = 30 + (Game.activeGame.scoreCounter.score-25);
				if (Game.generator.nextBoolean()) {
					createBarrel(75, speed, 0);
					createBarrel(250, speed, 3);
					
					createBarrel(500, speed-2, 45);
					createBarrel(290, speed+2, 47);
					
					createBarrel(75, speed+2, 83);
					createBarrel(220, speed-2, 80);
				} else {
					createBarrel(500, speed-2, 0);
					createBarrel(290, speed+2, 3);
					
					createBarrel(75, speed-2, 40);
					createBarrel(220, speed+2, 43);
					
					createBarrel(500, speed+2, 78);
					createBarrel(290, speed-2, 75);
				}
			}
		});
		obstacles.add(new Obstacle(7, 25) {
			@Override
			public void create() {
				super.create();
				if (Game.activeGame.scoreCounter.score < 16) {
					if (Game.generator.nextBoolean()) {
						createBarrel(500, 35, 20);
						createBarrel(250, 37, 40);
						createBarrel(75, 35, 65);
						createBarrel(500, 36, 80);
					} else {
						createBarrel(75, 35, 20);
						createBarrel(250, 37, 40);
						createBarrel(500, 35, 65);
						createBarrel(75, 36, 80);
					}
				} else {
					if (Game.generator.nextBoolean()) {
						createBarrel(500, 35, 20);
						createBarrel(250, 37, 35);
						createBarrel(75, 35, 50);
						createBarrel(500, 36, 65);
					} else {
						createBarrel(75, 35, 20);
						createBarrel(250, 37, 35);
						createBarrel(500, 35, 50);
						createBarrel(75, 36, 65);
					}
				}
			}
		});
		obstacles.add(new Obstacle(24, 30) {
			@Override
			public void create() {
				super.create();
				createBarrel(250, 35, 0);
				createBarrel(75, 33, 30);
				createBarrel(500, 33, 30);
				
				createBarrel(250, 35, 60);
				createBarrel(75, 33, 90);
				createBarrel(500, 33, 90);
			}
		});
		obstacles.add(new Obstacle(24, 30) {
			@Override
			public void create() {
				super.create();
				createBarrel(75, 35, 0);
				createBarrel(250, 37, 10);
				createBarrel(500, 35, 25);
			}
		});
		obstacles.add(new Obstacle(22, 35) {
			@Override
			public void create() {
				super.create();
				if (Game.generator.nextBoolean()) {
					createBarrel(500, 35, 20);
					createBarrel(250, 37, 35);
					createBarrel(75, 35, 50);
					createBarrel(500, 36, 65);
				} else {
					createBarrel(75, 35, 20);
					createBarrel(250, 37, 35);
					createBarrel(500, 35, 50);
					createBarrel(75, 36, 65);
				}
				
				if (Game.generator.nextBoolean()) {
					createBarrel(500, 35, 60+20);
					createBarrel(250, 37, 60+35);
					createBarrel(75, 35, 60+50);
					createBarrel(500, 36, 60+65);
				} else {
					createBarrel(75, 35, 60+20);
					createBarrel(250, 37, 60+35);
					createBarrel(500, 35, 60+50);
					createBarrel(75, 36, 60+65);
				}
			}
		});
		
		activeObstacle = obstacles.get(0);		
	}

	@Override
	public void tick() {
		if (!Game.activeGame.hippie.isDead && !paused) {
			// Do initial delay (For use at beginning of game)
			if (initialDelay > 0) {
				initialDelay --;
				return;
			} else if (initialDelay == 0) {
				// Decrement initialDelay by one, so this will not run repeatedly
				initialDelay --;
				// Create the first obstacle
				activeObstacle.create();
			}
			
			// Spawn balloon if necessary
			if (tickCount - lastBalloonSpawnTime > balloonSpawnDelay) {
				spawnBalloon();
				if (balloonSpawnDelay > balloonSpawnDelayMinimum)
					balloonSpawnDelay -= 2;
				lastBalloonSpawnTime = tickCount;
			}
			
			// Create an obstacle if necessary
			if (!activeObstacle.isActive() && tickCount % obstacleSpawnDelay == 0) {
				int score = Game.activeGame.scoreCounter.score;
				// Repeat until obstacle is created
				while (true) {
					// Integer 'i' is a random index within 'obstacles'
					int i = Game.generator.nextInt(obstacles.size());
					if (score > obstacles.get(i).scoreRange.x &&
							score < obstacles.get(i).scoreRange.y) {
						activeObstacle = obstacles.get(i);
						activeObstacle.create();
						break;
					}
				}
			}
			
			tickCount ++;
		}
	}
	
	public void reset() {
		balloonSpawnDelay = 100;
	}
	
	public void spawnBalloon() {
		new Balloon(getRandColor());
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