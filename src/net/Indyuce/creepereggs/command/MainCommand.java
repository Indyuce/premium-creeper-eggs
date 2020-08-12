package net.Indyuce.creepereggs.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;
import net.Indyuce.creepereggs.gui.CreepersList;

public class MainCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("premiumcreepereggs.admin")) {
			sender.sendMessage(ChatColor.RED + PremiumCreeperEggs.getInstance().getMessage("not-enough-perms"));
			return true;
		}

		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------[" + ChatColor.LIGHT_PURPLE + " PremiumCreeperEggs Help Page " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "]----------");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "<>" + ChatColor.GRAY + " = required");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "()" + ChatColor.GRAY + " = optional");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/pce list " + ChatColor.GRAY + "shows all available creeper eggs.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/pce give <egg> (player) (amount) " + ChatColor.GRAY + "gives a player an egg.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/pce reload " + ChatColor.GRAY + "reloads the plugin.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/pce version " + ChatColor.GRAY + "shows the plugin version.");
			return true;
		}

		if (args[0].equalsIgnoreCase("version")) {
			Bukkit.dispatchCommand(sender, "version " + PremiumCreeperEggs.getInstance().getName());
		}

		if (args[0].equalsIgnoreCase("list")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.GREEN + "Available eggs:");
				for (CreeperEgg egg : PremiumCreeperEggs.getInstance().getEggs().getAll())
					sender.sendMessage(ChatColor.WHITE + "- " + egg.getName() + " (" + egg.getId() + ")");
				return true;
			}
			new CreepersList((Player) sender).open();
		}

		if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
			PremiumCreeperEggs.getInstance().reloadConfig();
			PremiumCreeperEggs.getInstance().reloadConfigFiles();
			for (CreeperEgg egg : PremiumCreeperEggs.getInstance().getEggs().getAll())
				egg.update(PremiumCreeperEggs.getInstance().getEggsConfig().getConfigurationSection(egg.getId()));
			sender.sendMessage(ChatColor.YELLOW + "Eggs & config reloaded.");
		}

		if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Usage: /pce give <egg> (player) (amount)");
				return false;
			}

			if (args.length < 3 && !(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Please specify a player.");
				return false;
			}

			// egg
			String eggFormat = args[1].toUpperCase().replace("-", "_");
			CreeperEgg egg = PremiumCreeperEggs.getInstance().getEggs().get(eggFormat);
			if (egg == null) {
				sender.sendMessage(ChatColor.RED + eggFormat + " is not a valid egg.");
				return false;
			}

			// target
			Player t = args.length > 2 ? Bukkit.getPlayer(args[2]) : (Player) sender;
			if (t == null) {
				sender.sendMessage(ChatColor.RED + "Couldn't find a player called " + args[2] + ".");
				return false;
			}

			// amount
			int amount = 1;
			if (args.length > 3)
				try {
					amount = (int) Double.parseDouble(args[3]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + args[3] + " is not a valid number.");
					return false;
				}

			// void
			ItemStack i = egg.getItem();
			i.setAmount(amount);
			if (t.getInventory().firstEmpty() == -1)
				t.getWorld().dropItem(t.getLocation(), i);
			else
				t.getInventory().addItem(i);
			sender.sendMessage(ChatColor.YELLOW + "Successfully gave " + ChatColor.WHITE + egg.getName() + (amount > 1 ? " x" + amount : "") + ChatColor.YELLOW + " to " + ChatColor.WHITE + t.getName() + ChatColor.YELLOW + ".");

			// message
			String message = PremiumCreeperEggs.getInstance().getMessage("receive-egg");
			if (!message.equals("") && sender != t)
				t.sendMessage(ChatColor.YELLOW + message.replace("%item%", egg.getName() + (amount > 1 ? " x" + amount : "")));
		}
		return true;
	}
}
