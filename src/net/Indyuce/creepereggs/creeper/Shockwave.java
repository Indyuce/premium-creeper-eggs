package net.Indyuce.creepereggs.creeper;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class Shockwave extends CreeperEgg {
	public Shockwave() {
		super("Shockwave Creeper Egg", 10, "Greatly knocks hit entities back.");

		addValue("radius", 8);
		addValue("force", 1);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		double r = getValue("radius");
		double f = getValue("force");

		c.getWorld().spawnParticle(Particle.BLOCK_CRACK, c.getLocation(), 200, 2, .1, 2, 0, Material.DIRT.createBlockData());
		c.getWorld().playSound(c.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 2, 0);

		for (Entity entity : c.getNearbyEntities(r, r, r))
			if (entity instanceof LivingEntity)
				entity.setVelocity(entity.getLocation().toVector().subtract(c.getLocation().toVector()).setY(0).normalize().multiply(f).setY(f));
	}
}
