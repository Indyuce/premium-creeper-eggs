package net.Indyuce.creepereggs.comp.silkspawners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class VanillaSpawnerHandler implements SpawnerHandler {
	@Override
	public ItemStack getSpawner(Block block) {
		ItemStack item = new ItemStack(Material.SPAWNER);
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		meta.setBlockState(block.getState());
		item.setItemMeta(meta);
		return item;
	}
}
