package net.Indyuce.creepereggs.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class CreepersList extends PluginInventory {
	public CreepersList(Player player) {
		super(player);
	}

	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(this, 54, ChatColor.UNDERLINE + PremiumCreeperEggs.getInstance().getMessage("gui.title"));
		List<CreeperEgg> eggs = new ArrayList<CreeperEgg>(PremiumCreeperEggs.getInstance().getEggs().getAll());
		int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43 };

		ItemStack noEgg = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta noEggMeta = noEgg.getItemMeta();
		noEggMeta.setDisplayName(ChatColor.RED + PremiumCreeperEggs.getInstance().getMessage("gui.no-egg"));
		noEgg.setItemMeta(noEggMeta);

		for (int j = 0; j < 28; j++)
			inv.setItem(slots[j], j < eggs.size() ? eggs.get(j).getItem() : noEgg);

		return inv;
	}

	@Override
	public void whenClicked(ItemStack item) {
		if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + PremiumCreeperEggs.getInstance().getMessage("gui.no-egg")))
			return;

		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
		for (ItemStack drop : player.getInventory().addItem(item).values())
			player.getWorld().dropItem(player.getLocation(), drop);
	}
}
