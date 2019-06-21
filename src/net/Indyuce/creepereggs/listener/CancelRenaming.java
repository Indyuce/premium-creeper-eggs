package net.Indyuce.creepereggs.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;

public class CancelRenaming implements Listener {
	@EventHandler
	public void a(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		ItemStack i = e.getCurrentItem();
		
		if (inv != null)
			if (inv.getType() == InventoryType.ANVIL && e.getSlot() == 2)
				if (PremiumCreeperEggs.getInstance().getEggs().fromItem(i) != null)
					e.setCancelled(true);
	}
}
