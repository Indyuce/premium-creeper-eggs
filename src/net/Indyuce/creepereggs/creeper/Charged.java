package net.Indyuce.creepereggs.creeper;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Charged extends CreeperEgg {
	public Charged() {
		super("Charged Creeper Egg", 10, "Use this egg to summon", "a super charged creeper!");

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("xdx", "tet", "xtx");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('d', Material.DIAMOND);
		setRecipe(recipe);
	}

	@Override
	public void creatureSpawn(Creeper c) {
		c.setPowered(true);
	}
}
