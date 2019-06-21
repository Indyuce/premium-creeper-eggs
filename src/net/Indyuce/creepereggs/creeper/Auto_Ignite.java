package net.Indyuce.creepereggs.creeper;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Auto_Ignite extends CreeperEgg {
	public Auto_Ignite() {
		super("Auto Ignite Creeper Egg", 10, "Use this egg to summon", "a creeper that will explode by itself.");

		addValue("fuse-ticks", 100);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("xtx", "tet", "xtx");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		setRecipe(recipe);
	}

	@Override
	public void creatureSpawn(Creeper c) {
		explosionDelay(c, getValue("fuse-ticks"));
	}
}
