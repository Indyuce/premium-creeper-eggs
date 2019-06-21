package net.Indyuce.creepereggs.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.gui.PluginInventory;

public class GuiListener implements Listener {
	@EventHandler
	public void a(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof PluginInventory) {
			e.setCancelled(true);
			ItemStack i = e.getCurrentItem();
			if (e.getClickedInventory() == e.getInventory() && PremiumCreeperEggs.getInstance().hasDisplayName(i))
				((PluginInventory) e.getInventory().getHolder()).whenClicked(i);
		}
	}
}
