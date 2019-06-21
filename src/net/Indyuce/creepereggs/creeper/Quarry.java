package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Quarry extends CreeperEgg {
	public Quarry() {
		super("Quarry Creeper Egg", 300, "Use this egg to automatically", "dig down a tunnel.");

		addValue("size", 3);
		addValue("depth", 20);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("xpx", "tet", "xtx");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('p', Material.IRON_PICKAXE);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		fakeCreeperExplosion(e, c);
		new BukkitRunnable() {
			int j = 0;
			int s = (int) getValue("size");
			int d = (int) getValue("depth");
			Location loc = c.getLocation().getBlock().getLocation().add(.5 - s / 2, -1, .5 - s / 2);

			public void run() {
				int x = j % s;
				int y = -j / (s * s);
				int z = j / s % s;

				j++;
				if (y < -d) {
					cancel();
					return;
				}

				loc.add(x, y, z);
				Block b = loc.getBlock();
				if (!PremiumCreeperEggs.getInstance().isBlacklisted(b.getType()))
					b.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
				loc.subtract(x, y, z);

			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, 1);
	}
}
