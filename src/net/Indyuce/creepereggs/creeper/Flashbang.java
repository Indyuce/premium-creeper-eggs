package net.Indyuce.creepereggs.creeper;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.creepereggs.api.CreeperEgg;

public class Flashbang extends CreeperEgg {
	public Flashbang() {
		super("Flashbang Creeper Egg", 10, "Use this egg to blind", "oponents within a medium area.");

		addValue("radius", 10);
		addValue("duration", 10);
		addValue("slow", 2);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		double r = getValue("radius");
		int duration = (int) (getValue("duration") * 20);
		int slow = (int) getValue("slow") - 1;

		c.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, c.getLocation().add(0, 1, 0), 64, 0, 0, 0, .5);
		c.getWorld().playSound(c.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 5, 0);

		for (Entity t : c.getNearbyEntities(r, r, r))
			if (t instanceof LivingEntity) {
				((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, slow));
				((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 1));
				((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));
			}
	}
}
