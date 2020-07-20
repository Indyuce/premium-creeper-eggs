package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Sandstacker extends CreeperEgg {
	public Sandstacker() {
		super("Sandstacker Creeper Egg", 30, "Use this egg to stack sand", "blocks up to building limit.");

		addValue("delay", 3);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("sts", "tet", "sts");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('s', Material.SAND);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		fakeCreeperExplosion(e, c);
		new BukkitRunnable() {
			Location loc = c.getLocation();

			public void run() {
				while (loc.getY() < 256) {
					if (loc.getBlock().getType() != Material.AIR)
						break;

					loc.getBlock().setType(Material.SAND);
					loc.add(0, 1, 0);
				}
			}
		}.runTaskLater(PremiumCreeperEggs.getInstance(), (long) getValue("delay"));
	}
}
