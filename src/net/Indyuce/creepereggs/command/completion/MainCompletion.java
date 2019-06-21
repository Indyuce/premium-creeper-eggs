package net.Indyuce.creepereggs.command.completion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class MainCompletion implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("premiumcreepereggs.admin"))
			return null;

		List<String> list = new ArrayList<String>();
		
		if (args.length == 1) {
			list.add("give");
			list.add("reload");
			list.add("list");
			list.add("version");
		}

		else if (args[0].equalsIgnoreCase("give")) {
			if (args.length == 2)
				for (CreeperEgg egg : PremiumCreeperEggs.getInstance().getEggs().getAll())
					list.add(egg.getID());
			
			if (args.length == 3)
				for (Player target : Bukkit.getOnlinePlayers())
					list.add(target.getName());
			
			if (args.length == 4)
				for (int j = 1; j < 10; j++)
					list.add("" + j);
		}

		if (!args[args.length - 1].isEmpty()) {
			List<String> newList = new ArrayList<String>();
			for (String s : list)
				if (s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
					newList.add(s);
			list = newList;
		}

		return list;
	}
}
