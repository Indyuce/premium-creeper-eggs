package net.Indyuce.creepereggs.creeper;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Frag extends CreeperEgg {
	private Random random = new Random();

	public Frag() {
		super("Frag Creeper Egg", 10, "Use this egg to summon", "multiple auto-igniting creepers.");

		addValue("amount", 7);
		addValue("fuse-ticks", 100);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("tet", "eee", "tet");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		setRecipe(recipe);
	}

	@Override
	public void creatureSpawn(Creeper c) {
		c.remove();
		c.getWorld().spawnParticle(Particle.SMOKE_LARGE, c.getLocation().add(0, 1, 0), 16, 0, 0, 0, .13);
		c.getWorld().spawnParticle(Particle.FLAME, c.getLocation().add(0, 1, 0), 64, 0, 0, 0, 13);
		c.getWorld().spawnParticle(Particle.LAVA, c.getLocation().add(0, 1, 0), 16);
		for (int j = 0; j < getValue("amount"); j++) {
			Creeper t = (Creeper) c.getWorld().spawnEntity(c.getLocation(), EntityType.CREEPER);
			Vector v = new Vector((random.nextDouble() - .5) * .7, 1, (random.nextDouble() - .5) * .7);
			t.setVelocity(v);
			explosionDelay(t, getValue("fuse-ticks"));
		}
	}
}
