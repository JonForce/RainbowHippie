package core;

public class TimedEvent implements Tickable {
	
	private int ticksUntilCallback;
	
	public TimedEvent(int ticksUntilCallback) {
		this.ticksUntilCallback = ticksUntilCallback;
		Game.activeGame.toBeTicked.add(this);
	}

	@Override
	public void tick() {
		if (ticksUntilCallback > 0)
			ticksUntilCallback --;
		else {
			callback();
			dispose();
		}
	}
	
	public boolean isActive() {
		return Game.activeGame.toBeTicked.contains(this);
	}
	
	public void dispose() {
		Game.activeGame.toBeTicked.remove(this);
	}
	
	public void callback() { }
	
}