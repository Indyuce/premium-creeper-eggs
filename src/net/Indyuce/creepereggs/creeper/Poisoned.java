package net.Indyuce.creepereggs.creeper;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class Poisoned extends CreeperEgg {
	public Poisoned() {
		super("Poisoned Creeper Egg", 10, "Use this egg to poison", "oponents within a medium area.");

		addValue("radius", 10);
		addValue("duration", 7);
		addValue("amplifier", 3);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		double r = getValue("radius");
		int duration = (int) (getValue("duration") * 20);
		int pow = (int) (getValue("amplifier") - 1);

		c.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, c.getLocation().add(0, 1, 0), 32, 2, 2, 2, 0);
		c.getWorld().spawnParticle(Particle.SLIME, c.getLocation().add(0, 1, 0), 48, 2, 2, 2, 0);
		c.getWorld().playSound(c.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 3, 0);

		for (Entity t : c.getNearbyEntities(r, r, r))
			if (t instanceof LivingEntity)
				((LivingEntity) t).addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, pow));
	}
}
