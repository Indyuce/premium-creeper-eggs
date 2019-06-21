package net.Indyuce.creepereggs.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.version.nms.ItemTag;

public class CreeperEgg {
	private String[] lore;
	private String id, name, creeperName;
	private Map<String, Double> modifiers = new HashMap<String, Double>();
	private EggRecipe recipe;
	private boolean recipeEnabled;

	public CreeperEgg(String name, double cooldown, String... lore) {
		this(name.replace(" Creeper Egg", ""), name, name.replace(" Egg", ""), cooldown, lore);
	}

	public CreeperEgg(String id, String name, String creeperName, double cooldown, String... lore) {
		this.id = id.toUpperCase().replace("-", "_").replace(" ", "_").replaceAll("[^A-Z_]", "");
		this.name = name;
		this.creeperName = creeperName;
		this.lore = lore;

		addValue("cooldown", cooldown);
	}

	public String getID() {
		return id;
	}

	public String getLowerCaseID() {
		return id.toLowerCase().replace("_", "-");
	}

	public String getName() {
		return name;
	}

	public Set<String> getModifiers() {
		return modifiers.keySet();
	}

	public double getValue(String key) {
		return modifiers.get(key);
	}

	public String getCreeperName() {
		return creeperName;
	}

	public String[] getLore() {
		return lore;
	}

	public EggRecipe getRecipe() {
		return recipe;
	}

	public boolean hasRecipe() {
		return recipe != null;
	}

	public boolean isRecipeEnabled() {
		return recipeEnabled;
	}

	public void setRecipe(EggRecipe recipe) {
		this.recipe = recipe;
	}

	public void addValue(String path, double defaultValue) {
		modifiers.put(path, defaultValue);
	}

	public void creatureSpawn(Creeper creeper) {
	}

	public void entityExplode(ExplosionPrimeEvent event, Creeper creeper) {
	}

	public void fakeCreeperExplosion(ExplosionPrimeEvent event, Creeper creeper) {
		event.setCancelled(true);
		creeper.remove();

		Random rdm = new Random();
		Location loc = creeper.getLocation().add(0, 1, 0);

		loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0);
		loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 32, 0, 0, 0, .2);
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 4, (1f + (rdm.nextFloat() - rdm.nextFloat()) * .2f) * .7f);
	}

	public void explosionDelay(Creeper creeper, double duration) {
		new BukkitRunnable() {
			int ti = 0;
			ArmorStand as = (ArmorStand) creeper.getWorld().spawnEntity(creeper.getLocation().add(0, 2, 0), EntityType.ARMOR_STAND);

			public void run() {
				if (ti >= duration || creeper == null || creeper.isDead()) {
					as.remove();
					explodeCreeper(creeper);
					cancel();
					return;
				}

				// initialize armor stand
				if (ti == 0) {
					as.setVisible(false);
					as.setGravity(false);
					as.setCustomNameVisible(true);
					as.setMarker(true);
				}

				// entity can move
				// teleport back stand to entity for
				as.teleport(creeper.getLocation().add(0, 2, 0));

				if (ti % 6 == 0)
					creeper.getWorld().playSound(creeper.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, (float) (ti / duration) + 1);

				// update name
				as.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + new DecimalFormat("0.0").format((duration - (double) ti) / 20));
				ti++;
			}
		}.runTaskTimer(PremiumCreeperEggs.getInstance(), 0, 1);
	}

	// makes a creeper explode by setting its
	// max fuse ticks to a negative value
	public void explodeCreeper(Creeper creeper) {
		try {
			Object handle = creeper.getClass().getMethod("getHandle").invoke(creeper);
			Field field = Class.forName("net.minecraft.server." + PremiumCreeperEggs.getInstance().getVersion().toString() + "." + "EntityCreeper").getDeclaredField("maxFuseTicks");
			field.setInt(handle, -1);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void register() {
		PremiumCreeperEggs.getInstance().getEggs().registerEgg(this);
	}

	public void update(ConfigurationSection eggs) {
		name = eggs.getString("name");
		creeperName = eggs.getString("creeper-name");
		lore = eggs.getStringList("lore").toArray(new String[0]);

		for (String key : modifiers.keySet())
			modifiers.put(key, eggs.getDouble(key));

		recipe = EggRecipe.fromFormattedRecipe(this, eggs.getStringList("craft"));
		recipeEnabled = eggs.getBoolean("craft-enabled");
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + ChatColor.translateAlternateColorCodes('&', name));
		List<String> lore = new ArrayList<>();
		for (String s : this.lore)
			lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return PremiumCreeperEggs.getInstance().getNMS().addTag(item, new ItemTag("creeperEggId", getID()));
	}

	protected EggRecipe generateNewRecipe() {
		return new EggRecipe(this);
	}
}
