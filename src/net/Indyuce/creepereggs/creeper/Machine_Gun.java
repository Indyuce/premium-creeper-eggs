package net.Indyuce.creepereggs.creeper;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class Machine_Gun extends CreeperEgg {
	public Machine_Gun() {
		super("Machinegun Creeper Egg", 30, "Use this egg to cast", "a rapid fire of TNT.");

		addValue("amount", 6);
		addValue("ticks", 3);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		new BukkitRunnable() {
			Location loc = c.getLocation();
			int ti = 0;
			Random r = new Random();

			public void run() {
				if (ti++ > getValue("amount"))
					cancel();

				loc.getWorld().playSound(loc, Sound.ENTITY_ITEM_PICKUP, 2, 1);
				TNTPrimed t = (TNTPrimed) c.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
				Vector v = new Vector((r.nextDouble() - .5) * .7, 1, (r.nextDouble() - .5) * .7);
				t.setVelocity(v);
				t.setFuseTicks(80);
			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, (long) getValue("ticks"));
	}
}
