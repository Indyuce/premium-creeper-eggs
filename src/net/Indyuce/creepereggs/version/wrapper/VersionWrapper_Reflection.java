package net.Indyuce.creepereggs.version.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;

public class VersionWrapper_Reflection implements VersionWrapper {

	private Class<?> nms(String str) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + PremiumCreeperEggs.getInstance().getVersion().toString() + "." + str);
	}

	private Class<?> obc(String str) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + PremiumCreeperEggs.getInstance().getVersion().toString() + "." + str);
	}

	@Override
	public NBTItem getNBTItem(org.bukkit.inventory.ItemStack item) {
		return new NBTItem_Reflection(item);
	}

	/*
	 * TODO change it to PersistentDataContainer
	 */
	public class NBTItem_Reflection extends NBTItem {
		private Object nms, compound;
		private Class<?> craftItemStack;

		public NBTItem_Reflection(ItemStack item) {
			super(item);

			try {
				craftItemStack = obc("inventory.CraftItemStack");
				nms = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(craftItemStack, item);
				compound = (boolean) nms.getClass().getMethod("hasTag").invoke(nms) ? nms.getClass().getMethod("getTag").invoke(nms)
						: nms("NBTTagCompound").getConstructor().newInstance();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException | InstantiationException e) {
				e.printStackTrace();
			}
		}

		@Override
		public String getString(String path) {
			try {
				return (String) compound.getClass().getMethod("getString", String.class).invoke(compound, path);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public boolean hasTag(String path) {
			try {
				return (boolean) compound.getClass().getMethod("hasKey", String.class).invoke(compound, path);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public boolean getBoolean(String path) {
			try {
				return (boolean) compound.getClass().getMethod("getBoolean", String.class).invoke(compound, path);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public double getDouble(String path) {
			try {
				return (double) compound.getClass().getMethod("getDouble", String.class).invoke(compound, path);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return 0;
			}
		}

		@Override
		public int getInteger(String path) {
			try {
				return (int) compound.getClass().getMethod("getInt", String.class).invoke(compound, path);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return 0;
			}
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> {
				try {
					if (tag.getValue() instanceof Boolean)
						compound.getClass().getMethod("setBoolean", String.class, Boolean.TYPE).invoke(compound, tag.getPath(), tag.getValue());
					else if (tag.getValue() instanceof Double)
						compound.getClass().getMethod("setDouble", String.class, Double.TYPE).invoke(compound, tag.getPath(), tag.getValue());
					else if (tag.getValue() instanceof String)
						compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, tag.getPath(), tag.getValue());
					else if (tag.getValue() instanceof Integer)
						compound.getClass().getMethod("setInt", String.class, Integer.TYPE).invoke(compound, tag.getPath(), tag.getValue());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					e.printStackTrace();
				}
			});
			return this;
		}

		@Override
		public NBTItem removeTag(String... paths) {
			for (String path : paths)
				try {
					compound.getClass().getMethod("remove", String.class).invoke(compound, path);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					e.printStackTrace();
				}
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<String> getTags() {
			try {
				return (Set<String>) compound.getClass().getMethod("getKeys").invoke(compound);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public ItemStack toItem() {
			try {
				nms.getClass().getMethod("setTag", compound.getClass()).invoke(nms, compound);
				return (ItemStack) craftItemStack.getMethod("asBukkitCopy", nms.getClass()).invoke(craftItemStack, nms);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
