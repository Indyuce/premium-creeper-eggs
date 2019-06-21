package net.Indyuce.creepereggs.creeper;

import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class Lightning extends CreeperEgg {
	public Lightning() {
		super("Lightning Creeper Egg", 10, "Use this egg to summon lightning.");
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		c.getWorld().strikeLightning(c.getLocation());
	}
}
