package net.Indyuce.creepereggs.listener;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class CustomCreeperDrops implements Listener {
	private Random random = new Random();

	@EventHandler
	public void a(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() != EntityType.CREEPER)
			return;

		if (entity.getCustomName() == null)
			return;

		// check if egg exists & calculate chance to drop
		CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().fromName(entity.getCustomName());
		if (egg != null && random.nextDouble() <= PremiumCreeperEggs.getInstance().getDropsConfig().getDouble("custom-creeper." + egg.getID()) / 100)
			entity.getWorld().dropItemNaturally(entity.getLocation(), egg.getItem());
	}
}
