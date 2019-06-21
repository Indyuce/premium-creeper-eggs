package net.Indyuce.creepereggs.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.Indyuce.creepereggs.api.PlayerData;

public class PlayerListener implements Listener {
	public PlayerListener() {
		for (Player p : Bukkit.getOnlinePlayers())
			PlayerData.initialize(p);
	}

	@EventHandler
	public void a(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!PlayerData.has(p)) {
			PlayerData.initialize(p);
			return;
		}
		PlayerData.get(p).updatePlayer(p);
	}
}
