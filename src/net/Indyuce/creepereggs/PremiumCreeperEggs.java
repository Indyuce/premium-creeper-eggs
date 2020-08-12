package net.Indyuce.creepereggs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.command.MainCommand;
import net.Indyuce.creepereggs.command.completion.MainCompletion;
import net.Indyuce.creepereggs.comp.CreeperPlaceholders;
import net.Indyuce.creepereggs.comp.Metrics;
import net.Indyuce.creepereggs.comp.silkspawners.SilkSpawnerHandler;
import net.Indyuce.creepereggs.comp.silkspawners.SpawnerHandler;
import net.Indyuce.creepereggs.comp.silkspawners.VanillaSpawnerHandler;
import net.Indyuce.creepereggs.comp.worldguard.WGPlugin;
import net.Indyuce.creepereggs.comp.worldguard.WorldGuardOff;
import net.Indyuce.creepereggs.comp.worldguard.WorldGuardOn;
import net.Indyuce.creepereggs.gui.listener.GuiListener;
import net.Indyuce.creepereggs.listener.CancelRenaming;
import net.Indyuce.creepereggs.listener.CreeperListener;
import net.Indyuce.creepereggs.listener.CustomCreeperDrops;
import net.Indyuce.creepereggs.listener.PlayerListener;
import net.Indyuce.creepereggs.listener.VanillaCreeperDrops;
import net.Indyuce.creepereggs.manager.EggsManager;
import net.Indyuce.creepereggs.version.ServerVersion;
import net.Indyuce.creepereggs.version.SpigotPlugin;
import net.Indyuce.creepereggs.version.wrapper.VersionWrapper;
import net.Indyuce.creepereggs.version.wrapper.VersionWrapper_Reflection;

public class PremiumCreeperEggs extends JavaPlugin {

	// plugin system
	private static PremiumCreeperEggs plugin;
	private ServerVersion version;

	// interfaces
	private VersionWrapper nms;
	private WGPlugin wgPlugin;
	private SpawnerHandler spawnerHandler;

	// file configurations
	private FileConfiguration eggs;
	private FileConfiguration drops;

	// managers
	private EggsManager eggManager;

	public void onLoad() {
		plugin = this;

		version = new ServerVersion(Bukkit.getServer().getClass());
		eggManager = new EggsManager();

		// register WG flags before it is enabled
		wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard") == null ? new WorldGuardOff() : new WorldGuardOn();
	}

	public void onEnable() {
		new SpigotPlugin(62669, this).checkForUpdate();

		try {
			getLogger().log(Level.INFO, "Detected Bukkit Version: " + version.toString());
			nms = (VersionWrapper) Class.forName("net.Indyuce.creepereggs.version.wrapper.NMSHandler_" + version.toString().substring(1))
					.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			getLogger().log(Level.SEVERE, "Your server version is handled via reflection");
			nms = new VersionWrapper_Reflection();
		}

		new Metrics(this);

		// setup in eggs.yml
		FileConfiguration eggs = ConfigData.getCD(this, "", "eggs");
		for (CreeperEgg egg : getEggs().getAll()) {
			if (!eggs.contains(egg.getId())) {
				eggs.set(egg.getId() + ".name", egg.getName());
				eggs.set(egg.getId() + ".creeper-name", egg.getCreeperName());
				eggs.set(egg.getId() + ".lore", prepareLore(egg.getLore()));
				for (String s : egg.getModifiers())
					eggs.set(egg.getId() + "." + s, egg.getValue(s));
				if (egg.hasRecipe()) {
					eggs.set(egg.getId() + ".craft", egg.getRecipe().getFormattedRecipe());
					eggs.set(egg.getId() + ".craft-enabled", true);
				}
			}

			// update eggs
			egg.update(eggs.getConfigurationSection(egg.getId()));
		}
		ConfigData.saveCD(this, eggs, "", "eggs");

		// config files
		saveDefaultConfig();
		saveDefaultFile("drops");
		reloadConfigFiles();

		// register egg recipes only if recipe is enabled
		// must be placed after reloadConfigFiles()
		for (CreeperEgg egg : PremiumCreeperEggs.getInstance().getEggs().getAll())
			if (egg.hasRecipe())
				if (egg.isRecipeEnabled()) {
					ShapedRecipe recipe = egg.getRecipe().toShapedRecipe();
					if (recipe == null) {
						getLogger().log(Level.WARNING, "Couldn't register crafting recipe of " + egg.getId());
						continue;
					}
					Bukkit.addRecipe(recipe);
				}

		// listeners
		Bukkit.getPluginManager().registerEvents(new CreeperListener(), this);
		Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

		if (drops.getBoolean("enable-drops.custom-creeper"))
			Bukkit.getPluginManager().registerEvents(new CustomCreeperDrops(), this);

		if (drops.getBoolean("enable-drops.vanilla-creeper"))
			Bukkit.getPluginManager().registerEvents(new VanillaCreeperDrops(), this);

		if (getConfig().getBoolean("cancel-renaming"))
			Bukkit.getPluginManager().registerEvents(new CancelRenaming(), this);

		// silkspawners
		spawnerHandler = getServer().getPluginManager().getPlugin("SilkSpawners") != null ? new SilkSpawnerHandler() : new VanillaSpawnerHandler();
		if (spawnerHandler instanceof SilkSpawnerHandler)
			getLogger().log(Level.INFO, "Hooked onto SilkSpawners");

		// worldguard flags
		if (wgPlugin instanceof WorldGuardOn)
			getLogger().log(Level.INFO, "Hooked onto WorldGuard");

		// placeholders
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new CreeperPlaceholders().register();
			getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
		}

		getCommand("premiumcreepereggs").setExecutor(new MainCommand());
		getCommand("premiumcreepereggs").setTabCompleter(new MainCompletion());
	}

	public static PremiumCreeperEggs getInstance() {
		return plugin;
	}

	public File getJarFile() {
		return getFile();
	}

	public EggsManager getEggs() {
		return eggManager;
	}

	public VersionWrapper getVersionWrapper() {
		return nms;
	}

	public ServerVersion getVersion() {
		return version;
	}

	public WGPlugin getWorldGuard() {
		return wgPlugin;
	}

	public SpawnerHandler getSpawners() {
		return spawnerHandler;
	}

	public FileConfiguration getEggsConfig() {
		return eggs;
	}

	public FileConfiguration getDropsConfig() {
		return drops;
	}

	public String getMessage(String path) {
		return ChatColor.translateAlternateColorCodes('&', PremiumCreeperEggs.getInstance().getConfig().getString("message." + path));
	}

	public boolean isBlacklisted(Material material) {
		return PremiumCreeperEggs.getInstance().getConfig().getStringList("block-blacklist").contains(material.name());
	}

	private void saveDefaultFile(String path) {
		try {
			File file = new File(getDataFolder(), path + ".yml");
			if (!file.exists())
				Files.copy(getResource("default/" + path + ".yml"), file.getAbsoluteFile().toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadConfigFiles() {
		eggs = ConfigData.getCD(this, "", "eggs");
		drops = ConfigData.getCD(this, "", "drops");
	}

	public boolean hasDisplayName(ItemStack item) {
		if (item != null)
			if (item.hasItemMeta())
				return item.getItemMeta().hasDisplayName();
		return false;
	}

	public List<String> prepareLore(String[] array) {
		List<String> list = new ArrayList<String>();
		list.add("&8&m------------------------------");
		list.addAll(Arrays.asList(array));
		list.add("&8&m------------------------------");
		list.add("&8&l[&a&l*&8&l] &7Right-click to use!");
		return list;
	}
}
