package net.Indyuce.creepereggs.listener;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class VanillaCreeperDrops implements Listener {
	private Random random = new Random();

	@EventHandler
	public void a(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() != EntityType.CREEPER)
			return;

		// only works with vanilla creepers
		if (entity.getCustomName() != null)
			if (PremiumCreeperEggs.getInstance().getEggs().fromName(entity.getCustomName()) != null)
				return;

		if (random.nextDouble() > PremiumCreeperEggs.getInstance().getDropsConfig().getDouble("vanilla-creeper.chance") / 100)
			return;

		List<String> availableDrops = PremiumCreeperEggs.getInstance().getDropsConfig().getStringList("vanilla-creeper.droppable");
		String id = availableDrops.get(random.nextInt(availableDrops.size()));
		CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().get(id);
		if (egg != null)
			entity.getWorld().dropItemNaturally(entity.getLocation(), egg.getItem());
	}
}
