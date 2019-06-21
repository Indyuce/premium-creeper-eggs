package net.Indyuce.creepereggs.comp;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.api.PlayerData;

public class CreeperPlaceholders extends PlaceholderExpansion {
	private DecimalFormat oneDecimal = new DecimalFormat("0.#");

	@Override
	public String getAuthor() {
		return "Indyuce";
	}

	@Override
	public String getIdentifier() {
		return "premiumcreepereggs";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (identifier.startsWith("cooldown_")) {
			CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().get(identifier.substring(9).toUpperCase());
			if (egg != null)
				return oneDecimal.format(PlayerData.get(player).getRemainingCooldown(egg));
		}
		return null;
	}
}