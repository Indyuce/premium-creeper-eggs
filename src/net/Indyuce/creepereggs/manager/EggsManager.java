package net.Indyuce.creepereggs.manager;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.creepereggs.PremiumCreeperEggs;
import net.Indyuce.creepereggs.api.CreeperEgg;

public class EggsManager {

	// eggs registration
	private Map<String, CreeperEgg> eggs = new HashMap<String, CreeperEgg>();

	// when set to false, can't register eggs anymore.
	private boolean creeperRegistration = true;

	public EggsManager() {

		// register default creeper eggs
		try {
			JarFile file = new JarFile(PremiumCreeperEggs.getInstance().getJarFile());
			for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements();) {
				JarEntry jarEntry = entry.nextElement();
				String name = jarEntry.getName().replace("/", ".");

				// make sure this is a non anonymous class
				// use registerEgg(CreeperEgg) since
				// PremiumCreeperEggs.getInstance().getEggs() is not initialized yet
				if (name.endsWith(".class") && !name.contains("$") && name.startsWith("net.Indyuce.creepereggs.creeper."))
					registerEgg((CreeperEgg) Class.forName(name.substring(0, name.length() - 6)).newInstance());
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerEgg(CreeperEgg egg) {
		if (!canRegisterCreepers()) {
			PremiumCreeperEggs.getInstance().getLogger().log(Level.WARNING, "Couldn't register " + egg.getID() + ": make sure you register creeper eggs before PCE enables (using #onLoad & a soft dependency)");
			return;
		}

		eggs.put(egg.getID(), egg);
	}

	public void stopRegistration() {
		creeperRegistration = false;
	}

	public boolean canRegisterCreepers() {
		return creeperRegistration;
	}

	public Collection<CreeperEgg> getAll() {
		return eggs.values();
	}

	public CreeperEgg get(String key) {
		return eggs.containsKey(key) ? eggs.get(key) : null;
	}

	public CreeperEgg fromName(String name) {
		for (CreeperEgg egg : PremiumCreeperEggs.getInstance().getEggs().getAll())
			if (name.equals(ChatColor.BLUE + egg.getCreeperName()))
				return egg;
		return null;
	}

	public CreeperEgg fromItem(ItemStack item) {
		String tag = PremiumCreeperEggs.getInstance().getNMS().getStringTag(item, "creeperEggId");
		return tag.equals("") ? null : eggs.get(tag);
	}
}
