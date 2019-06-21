package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class Lucky extends CreeperEgg {
	public Lucky() {
		super("Lucky Creeper Egg", 0, "Use this egg to", "drop mob spawners.");

		addValue("radius", 6);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		Location loc = c.getLocation();
		int r = (int) getValue("radius");
		for (int x = -r; x < r; x++)
			for (int y = -r; y < r; y++)
				for (int z = -r; z < r; z++) {
					loc.add(x, y, z);
					if (loc.getBlock().getType() == Material.SPAWNER) {

						// get spawner item
						ItemStack i = PremiumCreeperEggs.getInstance().getSpawners().getSpawner(loc.getBlock());
						new BukkitRunnable() {
							public void run() {
								loc.getWorld().dropItemNaturally(c.getLocation(), i);
							}
						}.runTaskLater(PremiumCreeperEggs.getInstance(), 1);
						loc.getBlock().breakNaturally();
					}
					loc.subtract(x, y, z);
				}
	}
}
