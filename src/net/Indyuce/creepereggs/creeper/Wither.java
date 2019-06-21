package net.Indyuce.creepereggs.creeper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Wither extends CreeperEgg {
	public Wither() {
		super("Wither Creeper Egg", 100, "Use this egg to summon a", "wither boss for 15 secs!");

		addValue("cooldown", 150);
		addValue("fuse-ticks", 80);
		addValue("summon-time", 15);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("xnx", "tet", "xtx");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('n', Material.NETHER_STAR);
		setRecipe(recipe);
	}

	@Override
	public void creatureSpawn(Creeper c) {
		explosionDelay(c, getValue("fuse-ticks"));
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		org.bukkit.entity.Wither w = (org.bukkit.entity.Wither) c.getWorld().spawnEntity(c.getLocation(), EntityType.WITHER);
		new BukkitRunnable() {
			int timer = (int) getValue("summon-time");

			public void run() {
				if (w == null || w.isDead())
					cancel();

				if (timer < 1) {
					playEffect(w.getLocation().add(0, 1, 0));
					w.remove();
					cancel();
				}
				w.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "" + timer + "s left");
				timer--;
			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, 20);
	}

	private void playEffect(Location loc) {
		new BukkitRunnable() {
			double j = 0;

			public void run() {
				j += Math.PI / 12;
				if (j > Math.PI * 2)
					cancel();

				for (double k = 0; k < Math.PI * 2; k += Math.PI)
					loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(Math.cos(j + k) * 2, 0, Math.sin(j + k) * 2), 0);
			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, 1);
	}
}
