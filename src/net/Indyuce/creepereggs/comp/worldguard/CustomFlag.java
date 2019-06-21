package net.Indyuce.creepereggs.comp.worldguard;

public enum CustomFlag {
	CUSTOM_CREEPER_EGGS;

	public String getPath() {
		return name().toLowerCase().replace("_", "-");
	}
}
