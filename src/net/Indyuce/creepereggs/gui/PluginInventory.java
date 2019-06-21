package net.Indyuce.creepereggs.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PluginInventory implements InventoryHolder {
	protected Player player;

	public PluginInventory(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void whenClicked(ItemStack item) {
	}

	public void open() {
		getPlayer().openInventory(getInventory());
	}

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(this, 27, "Default Inventory");
	}
}
