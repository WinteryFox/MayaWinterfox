package com.winter.mayawinterfox.data.item;

public class Food {

	private final int id;
	private final String name;
	private final int value;
	private final int cooldown;
	private final double multiplier;
	private final double chance;

	public Food(int id, String name, int value, int cooldown, double multiplier, double chance) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.cooldown = cooldown;
		this.multiplier = multiplier;
		this.chance = chance;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public int getCooldown() {
		return cooldown;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public double getChance() {
		return chance;
	}
}
