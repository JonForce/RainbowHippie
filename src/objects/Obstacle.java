package objects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import core.Game;
import core.Tickable;
import core.TimedEvent;

public class Obstacle implements Tickable {
	
	private ArrayList<Barrel> activeBarrels;
	private ArrayList<TimedEvent> activeTimedEvents;
	public Vector2 scoreRange;
	
	public Obstacle(int a, int b) {
		scoreRange = new Vector2(a, b);
		activeBarrels = new ArrayList<Barrel>();
		activeTimedEvents = new ArrayList<TimedEvent>();
		Game.activeGame.toBeTicked.add(this);
	}
	
	@Override
	public void tick() {
		// Check to see if any barrels are active
		boolean anyBarrelsActive = false;
		for (Barrel barrel : activeBarrels)
			if (barrel.isActive())
				anyBarrelsActive = true;
		
		// Check to see if any barrels could be created by a timed event
		boolean anyTimersActive = false;
		for (TimedEvent event : activeTimedEvents)
			if (event.isActive())
				anyTimersActive = true;
		
		// If no barrels exist and there is no potential for one to exist
		if (!anyBarrelsActive && !anyTimersActive)
			Game.activeGame.toBeTicked.remove(this);
	}
	
	public boolean isActive() {
		return Game.activeGame.toBeTicked.contains(this);
	}
	
	/*
	 * Should be overriden, create barrels with "createBarrel(...)"
	 */
	public void create() {
		Game.activeGame.toBeTicked.add(this);
	}
	
	public void dispose() {
		for (Barrel barrel : activeBarrels)
			barrel.dispose();
		for (TimedEvent event : activeTimedEvents)
			event.dispose();
		Game.activeGame.toBeTicked.remove(this);
	}
	
	/*
	 * Create a barrel at location "y", with velocity "speed", in "ticksTilCreation" ticks.
	 */
	protected void createBarrel(final int y, final float speed, int ticksTilCreation) {
		activeTimedEvents.add(new TimedEvent(ticksTilCreation) {
			@Override
			public void callback() {
				if (!Game.activeGame.hippie.isDead) {
					Barrel newBarrel = new Barrel(y) {
						@Override
						public void dispose() {
							super.dispose();
							activeBarrels.remove(this);
						}
					};
					newBarrel.velocity = speed;
					activeBarrels.add(newBarrel);
				}
			}
		});
	}
	
}