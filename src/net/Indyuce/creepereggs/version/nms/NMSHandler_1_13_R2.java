package net.Indyuce.creepereggs.version.nms;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class NMSHandler_1_13_R2 implements NMSHandler {
	@Override
	public ItemStack addTag(ItemStack item, ItemTag... tags) {
		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();

		for (ItemTag tag : tags) {
			if (tag.getValue() instanceof Boolean) {
				compound.setBoolean(tag.getPath(), (boolean) tag.getValue());
				continue;
			}
			if (tag.getValue() instanceof Double) {
				compound.setDouble(tag.getPath(), (double) tag.getValue());
				continue;
			}
			compound.setString(tag.getPath(), (String) tag.getValue());
		}

		nmsi.setTag(compound);
		item = CraftItemStack.asBukkitCopy(nmsi);
		return item;
	}

	@Override
	public Set<String> getTags(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return new HashSet<String>();

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getKeys();
	}

	@Override
	public boolean getBooleanTag(ItemStack item, String path) {
		if (item == null || item.getType() == Material.AIR)
			return false;

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getBoolean(path);
	}

	@Override
	public double getDoubleTag(ItemStack item, String path) {
		if (item == null || item.getType() == Material.AIR)
			return 0;

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getDouble(path);
	}

	@Override
	public String getStringTag(ItemStack item, String path) {
		if (item == null || item.getType() == Material.AIR)
			return "";

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getString(path);
	}
}
