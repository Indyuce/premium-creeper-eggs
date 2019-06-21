package net.Indyuce.creepereggs.creeper;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.EggRecipe;

public class Frozen extends CreeperEgg {
	public Frozen() {
		super("Frozen Creeper Egg", 30, "Use this egg to entirely", "freeze a large area.");

		addValue("radius", 7);
		addValue("amplifier", 1);
		addValue("duration", 4);

		EggRecipe recipe = generateNewRecipe();
		recipe.shape("iti", "tet", "iti");
		recipe.setIngredient('t', Material.TNT);
		recipe.setIngredient('e', Material.EGG);
		recipe.setIngredient('i', Material.ICE);
		setRecipe(recipe);
	}

	@Override
	public void entityExplode(ExplosionPrimeEvent e, Creeper c) {
		Location ctr = c.getLocation();
		Location loc = c.getLocation();
		int r = (int) getValue("radius");
		int duration = (int) (getValue("duration") * 20);
		int pow = (int) (getValue("amplifier") - 1);
		Random rd = new Random();

		fakeCreeperExplosion(e, c);

		// freezes the ground
		for (int x = -r; x < r; x++)
			for (int y = -r; y < r; y++)
				for (int z = -r; z < r; z++) {
					loc.add(x, y, z);
					if (loc.distanceSquared(ctr) <= Math.pow(r, 2)) {
						Block b = loc.getBlock();
						if (canBeFrozen(b.getType()) && !PremiumCreeperEggs.getInstance().isBlacklisted(b.getType()))
							b.setType(rd.nextDouble() < .15 ? Material.PACKED_ICE : Material.ICE);
					}
					loc.subtract(x, y, z);
				}

		// slow
		for (Entity t : c.getNearbyEntities(r, r, r))
			if (t instanceof LivingEntity)
				((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, pow));
	}

	private boolean canBeFrozen(Material m) {
		return m.isSolid() || m == Material.WATER;
	}
}
