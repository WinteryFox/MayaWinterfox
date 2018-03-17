package com.winter.mayawinterfox.data.item;

public class Item {

	private final int id;
	private final ItemProvider.ItemType type;
	private final String name;
	private final String path;
	private final boolean forSale;
	private final int coins;
	private final int gems;

	public Item(int id, ItemProvider.ItemType type, String name, String path, boolean forSale, int coins, int gems) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.path = path;
		this.forSale = forSale;
		this.coins = coins;
		this.gems = gems;
	}

	public int getId() {
		return id;
	}

	public ItemProvider.ItemType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean isForSale() {
		return forSale;
	}

	public int getCoins() {
		return coins;
	}

	public int getGems() {
		return gems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item)) return false;

		Item item = (Item) o;

		return getId() == item.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}
}