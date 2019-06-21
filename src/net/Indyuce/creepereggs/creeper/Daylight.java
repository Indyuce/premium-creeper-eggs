package net.Indyuce.creepereggs.creeper;

import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class Daylight extends CreeperEgg {
	public Daylight() {
		super("Daylight Creeper Egg", 100, "Use this egg to set day time.");
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		c.getWorld().setTime(3000);
		c.getWorld().playSound(c.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 3, 2);
	}
}
