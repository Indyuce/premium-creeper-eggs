package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Fiery extends CreeperEgg {
	public Fiery() {
		super("Fiery Creeper Egg", 30, "Use this egg to powerfully", "ignite a large area.");

		addValue("radius", 5);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("ftf", "tet", "ftf");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('f', Material.FLINT);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		Location ctr = c.getLocation();
		Location loc = c.getLocation();
		int r = (int) getValue("radius");

		fakeCreeperExplosion(e, c);

		for (int x = -r; x < r; x++)
			for (int y = -r; y < r; y++)
				for (int z = -r; z < r; z++) {
					loc.add(x, y, z);
					if (ctr.distanceSquared(loc) <= Math.pow(r, 2)) {
						Block b = loc.getBlock();
						if (b.getType() == Material.AIR)
							b.setType(Material.FIRE);
					}
					loc.subtract(x, y, z);
				}

	}
}
