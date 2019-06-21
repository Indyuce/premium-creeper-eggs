package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Midas extends CreeperEgg {
	public Midas() {
		super("Midas Creeper Egg", 300, "Use this egg to transform", "your surroundings into gold.");

		addValue("radius", 7);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("gtg", "tet", "gtg");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('g', Material.GOLD_BLOCK);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		Location ctr = c.getLocation();
		Location loc = c.getLocation();
		int r = (int) getValue("radius");

		fakeCreeperExplosion(e, c);
		c.getWorld().playSound(c.getLocation(), Sound.BLOCK_ANVIL_LAND, 3, 2);

		for (int x = -r; x < r; x++)
			for (int y = -r; y < r; y++)
				for (int z = -r; z < r; z++) {
					loc.add(x, y, z);
					if (loc.distanceSquared(ctr) <= Math.pow(r, 2)) {
						Block b = loc.getBlock();
						if (b.getType().isSolid() && !PremiumCreeperEggs.getInstance().isBlacklisted(b.getType()))
							b.setType(Material.GOLD_BLOCK);
					}
					loc.subtract(x, y, z);
				}
	}
}
