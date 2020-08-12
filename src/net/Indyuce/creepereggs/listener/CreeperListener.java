package net.Indyuce.creepereggs.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.PlayerData;
import net.Indyuce.creepereggs.api.event.CreeperEggUseEvent;
import net.Indyuce.creepereggs.api.event.CreeperExplodeEvent;

public class CreeperListener implements Listener {
	@EventHandler
	public void a(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!event.hasItem())
			return;

		ItemStack item = event.getItem();
		CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().fromItem(item);
		if (egg == null)
			return;

		/*
		 * cancel interact to summon a custom creeper after.
		 */
		event.setCancelled(true);
		if (!event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		// api
		Location loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().add(.5, 0, .5);
		CreeperEggUseEvent creeperEvent = new CreeperEggUseEvent(player, loc, egg);
		Bukkit.getPluginManager().callEvent(creeperEvent);
		egg = creeperEvent.getCreeperEgg();
		loc = creeperEvent.getSpawnLocation();
		if (creeperEvent.isCancelled())
			return;

		// check for permission
		if (!player.hasPermission("premiumcreepereggs.use." + egg.getLowerCaseId())) {
			player.sendMessage(PremiumCreeperEggs.getInstance().getMessage("not-enough-perms-egg"));
			return;
		}

		// check cooldown & worldGuard
		if (!PlayerData.get(player).canUse(egg))
			return;

		// spawn creeper
		Creeper creeper = (Creeper) player.getWorld().spawnEntity(loc, EntityType.CREEPER);
		creeper.setCustomName(ChatColor.BLUE + egg.getCreeperName());
		creeper.setCustomNameVisible(true);

		egg.creatureSpawn(creeper);

		// consume item
		if (player.getGameMode() != GameMode.CREATIVE) {
			if (item.getAmount() > 1)
				item.setAmount(item.getAmount() - 1);
			else
				player.getInventory().remove(item);
		}
	}

	@EventHandler
	public void b(ExplosionPrimeEvent event) {
		Entity player = event.getEntity();
		if (player.getType() != EntityType.CREEPER || player.getCustomName() == null)
			return;

		CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().fromName(player.getCustomName());
		if (egg == null)
			return;

		CreeperExplodeEvent creeperEvent = new CreeperExplodeEvent((Creeper) player, egg);
		Bukkit.getPluginManager().callEvent(creeperEvent);
		egg = creeperEvent.getCreeperEgg();
		if (creeperEvent.isCancelled()) {
			creeperEvent.setCancelled(true);
			player.remove();
			return;
		}

		egg.entityExplode(event, (Creeper) player);
	}
}
