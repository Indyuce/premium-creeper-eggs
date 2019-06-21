package net.Indyuce.creepereggs.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import net.Indyuce.creepereggs.PremiumCreeperEggs;

/*
 * this class cannot extend ShapedRecipe since ShapedRecipe requires an item
 * in its constructor and the item can't be generated yet
 */
public class EggRecipe {
	private CreeperEgg egg;
	private String[] shape;
	private Map<Character, Material> ingredients = new HashMap<>();

	public EggRecipe(CreeperEgg egg) {
		this.egg = egg;
	}

	public void shape(String... shape) {
		if (shape.length == 3)
			this.shape = shape;
	}

	public List<String> getFormattedRecipe() {
		List<String> formattedRecipe = new ArrayList<String>();

		for (String shapeLine : shape) {
			String formattedLine = "";
			for (int j = 0; j < 3; j++) {
				char char1 = shapeLine.charAt(j);
				Material item = ingredients.containsKey(char1) ? ingredients.get(char1) : Material.AIR;
				formattedLine += (formattedLine.isEmpty() ? "" : " ") + item.name();
			}
			formattedRecipe.add(formattedLine);
		}

		return formattedRecipe;
	}

	public void setIngredient(char character, Material material) {
		ingredients.put(character, material);
	}

	@SuppressWarnings("deprecation")
	public ShapedRecipe toShapedRecipe() {
		ShapedRecipe recipe = PremiumCreeperEggs.getInstance().getVersion().isBelowOrEqual(1, 11) ? new ShapedRecipe(egg.getItem()) : new ShapedRecipe(new NamespacedKey(PremiumCreeperEggs.getInstance(), "PremiumCreeperEggs_" + egg.getID()), egg.getItem());
		recipe.shape(shape);
		for (char char1 : ingredients.keySet())
			recipe.setIngredient(char1, ingredients.get(char1));
		return recipe;
	}

	public String[] getShape() {
		return shape;
	}

	public Map<Character, Material> getIngredients() {
		return ingredients;
	}

	public static EggRecipe fromFormattedRecipe(CreeperEgg egg, List<String> list) {
		EggRecipe recipe = egg.generateNewRecipe();
		recipe.shape("abc", "def", "ghi");

		if (list.size() < 3)
			return null;

		char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' };
		for (int j = 0; j < 9; j++) {
			char c = chars[j];
			List<String> line = Arrays.asList(list.get(j / 3).split("\\ "));
			if (line.size() < 3)
				return null;

			String s = line.get(j % 3);
			Material material = null;
			try {
				material = Material.valueOf(s.split("\\:")[0].replace("-", "_").toUpperCase());
			} catch (Exception e1) {
				return null;
			}

			if (material != Material.AIR)
				recipe.setIngredient(c, material);
		}

		return recipe;
	}
}
