package net.Indyuce.creepereggs.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class CreeperEggUseEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private CreeperEgg egg;
	private Location spawnLocation;
	private boolean cancelled = false;

	public CreeperEggUseEvent(Player player, Location spawnLocation, CreeperEgg egg) {
		this.player = player;
		this.spawnLocation = spawnLocation;
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

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location value) {
		spawnLocation = value;
	}

	public void setCreeperEgg(CreeperEgg value) {
		egg = value;
	}

	public Player getPlayer() {
		return player;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
