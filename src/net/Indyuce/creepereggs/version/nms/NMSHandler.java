package net.Indyuce.creepereggs.version.nms;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	public ItemStack addTag(ItemStack item, ItemTag... tags);

	public Set<String> getTags(ItemStack item);

	public double getDoubleTag(ItemStack item, String path);

	public String getStringTag(ItemStack item, String path);

	public boolean getBooleanTag(ItemStack item, String path);
}
