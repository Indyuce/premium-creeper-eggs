package net.Indyuce.creepereggs.creeper;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Nuke extends CreeperEgg {
	public Nuke() {
		super("Nuke Creeper Egg", 20, "Use this egg to entirely", "obliterate a large area.");

		addValue("radius", 10);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("ttt", "tet", "ttt");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		setRecipe(recipe);
	}

	@Override
	public void creatureSpawn(Creeper c) {
		c.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4));
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		c.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		e.setRadius((float) getValue("radius"));
	}
}
