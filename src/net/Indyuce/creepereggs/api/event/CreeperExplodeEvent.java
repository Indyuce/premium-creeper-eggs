package net.Indyuce.creepereggs.api.event;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class CreeperExplodeEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Creeper creeper;
	private CreeperEgg egg;
	private boolean cancelled = false;

	public CreeperExplodeEvent(Creeper creeper, CreeperEgg egg) {
		this.creeper = creeper;
		this.egg = egg;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean bool) {
		cancelled = bool;
	}

	public CreeperEgg getCreeperEgg() {
		return egg;
	}

	public void setCreeperEgg(CreeperEgg egg) {
		this.egg = egg;
	}

	public Creeper getCreeper() {
		return creeper;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
