package net.Indyuce.creepereggs.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Smoker extends CreeperEgg {
	public Smoker() {
		super("Smoker Creeper Egg", 10, "Use this egg to summon", "a dense smoke cloud.");

		addValue("duration", 4);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("ctc", "tet", "ctc");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('c', Material.COAL);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		new BukkitRunnable() {
			int ti = 0;
			double r = 3;
			double duration = getValue("duration") * 20 / 3;
			Location loc = c.getLocation();

			public void run() {
				if (ti++ > duration)
					cancel();

				for (double j = 0; j < Math.PI * 2; j += Math.PI / 4)
					loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(Math.cos(j) * r, 1, Math.sin(j) * r), 30, 0, 0, 0, .1);
			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, 3);
	}
}
