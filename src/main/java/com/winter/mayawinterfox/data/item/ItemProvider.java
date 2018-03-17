package com.winter.mayawinterfox.data.item;

import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.impl.ItemNotOwnedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemProvider {

	public static List<Food> foods = new ArrayList<>();
	public static List<Item> items = new ArrayList<>();
	private static Logger LOGGER = LoggerFactory.getLogger(ItemProvider.class);

	/**
	 * Load all foods into memory from the foods.json file
	 */
	public static void loadFoods() {
		items.clear();
		try (InputStream input = ItemProvider.class.getResourceAsStream("/foods.json")) {
			JSONTokener tokenizer = new JSONTokener(input);
			JSONArray root = new JSONArray(tokenizer);

			int loaded = 0;
			for (Object i : root) {
				if (i instanceof JSONObject) {
					JSONObject food = (JSONObject) i;
					foods.add(new Food(food.getInt("id"),
							food.getString("name"),
							food.getInt("value"),
							food.getInt("cooldown"),
							food.getDouble("multiplier"),
							food.getDouble("chance")));
					loaded++;
				}
			}
			LOGGER.info(String.format("Loaded %s food(s) from foods.json", loaded));
		} catch (Exception e) {
			LOGGER.error("Failed to read from foods.json", e);
			System.exit(1);
		}
	}

	/**
	 * Load all items into memory from the items.json file
	 */
	public static void loadItems() {
		items.clear();
		try (InputStream input = ItemProvider.class.getResourceAsStream("/items.json")) {
			JSONTokener tokenizer = new JSONTokener(input);
			JSONArray root = new JSONArray(tokenizer);

			int loaded = 0;
			for (Object i : root) {
				if (i instanceof JSONObject) {
					JSONObject item = (JSONObject) i;
					items.add(new Item(item.getInt("id"),
									ItemType.valueOf(item.getString("type")),
									item.getString("name"),
									item.getString("path"),
									item.getBoolean("forSale"),
									item.getInt("coins"),
									item.getInt("gems")));
					loaded++;
				}
			}
			LOGGER.info(String.format("Loaded %s item(s) from items.json", loaded));
		} catch (Exception e) {
			LOGGER.error("Failed to read from items.json", e);
			System.exit(1);
		}
	}

	/**
	 * Get an item by id or name
	 * @param s The string to search the item for
	 * @return The item that was found
	 * @throws ItemNotOwnedException When the person doesn't own the item
	 */
	public static Food getFood(String s) {
		if (s.matches("\\d+")) {
			int id = Integer.parseUnsignedInt(s);
			return foods.stream().filter(i -> i.getId() == id).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown food."));
		} else {
			return foods.stream().filter(i -> i.getName().toLowerCase().contains(s.toLowerCase())).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown food."));
		}
	}

	/**
	 * Get an item by its id
	 *
	 * @param id The id of the item
	 * @return The item with that id
	 * @throws IllegalArgumentException if no such item exists
	 */
	public static Item getItemById(int id) {
		return items.stream().filter(i -> i.getId() == id).findFirst().orElse(new Item(0, null, "", "/wolf/blank.png", false, 0, 0));
	}

	/**
	 * Get a list of items by name
	 * @param name The name to search for
	 * @return The item list
	 */
	public static List<Item> getItemsByName(String name) {
		return items.stream().filter(i -> i.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
	}

	/**
	 * Get a list of items by type
	 *
	 * @param type The type of the item
	 * @return The item list
	 */
	public static List<Item> getItemsByType(ItemProvider.ItemType type) {
		return items.stream().filter(i -> i.getType() == type).collect(Collectors.toList());
	}

	public enum ItemType {
		FOOD("food"),
		BACKGROUND("background"),
		HAT("hat"),
		BODY("body"),
		PAWS("paws"),
		TAIL("tail"),
		SHIRT("shirt"),
		NOSE("nose"),
		EYES("eyes"),
		NECK("neck");

		private String typeName;

		ItemType(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}
	}
}
