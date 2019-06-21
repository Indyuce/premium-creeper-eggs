package net.Indyuce.creepereggs.api;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.comp.worldguard.CustomFlag;

public class PlayerData {
	private static Map<UUID, PlayerData> map = new HashMap<>();

	private Player player;
	private Map<CreeperEgg, Long> cd = new HashMap<>();

	public PlayerData(Player player) {
		this.player = player;
	}

	public static void initialize(Player p) {
		map.put(p.getUniqueId(), new PlayerData(p));
	}

	public static boolean has(Player p) {
		return map.containsKey(p.getUniqueId());
	}

	public static PlayerData get(Player p) {
		return map.get(p.getUniqueId());
	}

	// the player instance doesn't work when the player logs off
	public void updatePlayer(Player player) {
		this.player = player;
	}

	public boolean canUse(CreeperEgg egg) {

		// worldguard flag
		if (!PremiumCreeperEggs.getInstance().getWorldGuard().isFlagAllowed(player, CustomFlag.CUSTOM_CREEPER_EGGS)) {
			player.sendMessage(ChatColor.RED + PremiumCreeperEggs.getInstance().getMessage("worldguard-flag"));
			return false;
		}

		// cooldown
		long lastUsed = cd.containsKey(egg) ? cd.get(egg) : 0;
		double remain = (double) lastUsed / 1000. + egg.getValue("cooldown") - (double) System.currentTimeMillis() / 1000.;
		if (remain > 0) {
			player.sendMessage(ChatColor.RED + PremiumCreeperEggs.getInstance().getMessage("cooldown").replace("%s%", remain >= 2 ? "s" : "").replace("%remain%", new DecimalFormat("0.0").format(remain)));
			return false;
		}

		// apply cd
		cd.put(egg, System.currentTimeMillis());
		return true;
	}

	/*
	 * returns the cooldown in seconds
	 */
	public double getRemainingCooldown(CreeperEgg egg) {
		return Math.max(0, (cd.containsKey(egg) ? cd.get(egg) : 0) + 1000 * egg.getValue("cooldown") - System.currentTimeMillis()) / 1000;
	}
}
