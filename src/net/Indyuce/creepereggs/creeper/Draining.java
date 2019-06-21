package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Draining extends CreeperEgg {
	public Draining() {
		super("Draining Creeper Egg", 20, "Use this egg to drain all", "water within a small area.");

		addValue("radius", 6);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("wtw", "tet", "wtw");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('w', Material.WATER_BUCKET);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent event, Creeper creeper) {
		Location ctr = creeper.getLocation();
		Location loc = creeper.getLocation();
		int r = (int) getValue("radius");

		// removes any water block
		for (int x = -r; x < r; x++)
			for (int y = -r; y < r; y++)
				for (int z = -r; z < r; z++) {
					loc.add(x, y, z);
					if (ctr.distanceSquared(loc) <= Math.pow(r, 2)) {
						Block b = loc.getBlock();
						if (b.getType() == Material.WATER)
							b.setType(Material.AIR);
					}
					loc.subtract(x, y, z);
				}
	}
}
