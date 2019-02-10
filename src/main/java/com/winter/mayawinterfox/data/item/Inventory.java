package com.winter.mayawinterfox.data.item;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.ItemNotOwnedException;
import discord4j.core.object.entity.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

public class Inventory {

	private User user;
	private Set<Item> items;

	private Inventory(User user, Set<Item> items) {
		this.user = user;
		this.items = items;
	}

	@NotNull
	public static Mono<Inventory> create(@NotNull User user) {
		return Database.get("SELECT item FROM item WHERE id=?", user.getId().asLong())
				.map(v -> ItemProvider.getItemById((int) v.get("item")))
				.collect(Collectors.toSet())
				.map(i -> new Inventory(user, i));
	}

	/**
	 * Get the user this inventory belongs to
	 *
	 * @return The user the inventory belongs to
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Add an item to the inventory
	 * @param item The item to add to the inventory
	 * @return True on success false on failure
	 */
	public Mono<Boolean> addItem(Item item) {
		return Database.set("INSERT IGNORE INTO item (id, item) VALUES (?, ?)", user, item.getId())
				.map(v -> items.add(item));
	}

	/**
	 * Remove an item from the inventory
	 * @param item The item to remove from the inventory
	 * @return True on success false on failure
	 */
	public Mono<Boolean> removeItem(Item item) {
		return Database.set("DELETE IGNORE FROM item WHERE id=? AND item=?;", user.getId().asLong(), item.getId())
				.map(v -> items.remove(item));
	}

	/**
	 * Get an item by id or name
	 * @param s The string to search the item for
	 * @return The item that was found
	 */
	public Item getItem(String s) {
		if (s.matches("\\d+")) {
			int id = Integer.parseUnsignedInt(s);
			return getItemById(id);
		} else {
			return getItemsByName(s).stream().findFirst().orElse(null);
		}
	}

	/**
	 * Get an item by its id
	 *
	 * @param id The id of the item
	 * @return The item with that id
	 * @throws ItemNotOwnedException if no such item exists
	 */
	private Item getItemById(int id) {
		return id == 0 ? null : items.stream().filter(i -> i.getId() == id).findFirst().orElseThrow(ItemNotOwnedException::new);
	}

	/**
	 * Get all items in the inventory
	 *
	 * @return The items in the inventory
	 */
	public Set<Item> getItems() {
		return items;
	}

	/**
	 * Get a list of items by name
	 * @param name The name to search for
	 * @return The item list
	 */
	public Set<Item> getItemsByName(String name) {
		return items.stream().filter(i -> i.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
	}

	/**
	 * Get all items by type
	 *
	 * @param type The type to filter for
	 * @return The filtered result in a list
	 */
	public Set<Item> getItemsByType(ItemProvider.ItemType type) {
		return items.stream().filter(i -> i.getType() == type).collect(Collectors.toSet());
	}
}