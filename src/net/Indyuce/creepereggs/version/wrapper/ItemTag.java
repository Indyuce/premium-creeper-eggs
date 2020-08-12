package net.Indyuce.creepereggs.version.wrapper;

public class ItemTag {
	private String path;
	private Object value;

	public ItemTag(String path, Object value) {
		this.path = path;
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public Object getValue() {
		return value;
	}
}