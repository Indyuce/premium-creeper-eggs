package net.Indyuce.creepereggs.comp.silkspawners;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import de.dustplanet.util.SilkUtil;

public class SilkSpawnerHandler implements SpawnerHandler {
	private static SilkUtil util = SilkUtil.hookIntoSilkSpanwers();

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getSpawner(Block block) {
		EntityType type = ((CreatureSpawner) block.getState()).getSpawnedType();
		return util.newSpawnerItem(type.getTypeId(), type.getName());
	}
}
