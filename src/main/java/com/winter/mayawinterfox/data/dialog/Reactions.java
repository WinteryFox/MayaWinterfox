package com.winter.mayawinterfox.data.dialog;

import java.util.HashMap;
import java.util.Map;

public enum Reactions {
	ONE(1, "one"),
	TWO(2, "two"),
	THREE(3, "three"),
	FOUR(4, "four"),
	FIVE(5, "five"),
	SIX(6, "six"),
	SEVEN(7, "seven"),
	EIGHT(8, "eight"),
	NINE(9, "nine"),
	TEN(10, "keycap_ten"),
	;

	private int value;
	private String emoji;

	private static Map<Integer, Reactions> map = new HashMap<>();

	static {
		for (Reactions reaction : Reactions.values())
			map.put(reaction.value, reaction);
	}

	Reactions(int value, String emoji) {
		this.value = value;
		this.emoji = emoji;
	}

	public int getValue() {
		return value;
	}

	public String getEmoji() {
		return emoji;
	}

	public static Reactions valueOf(int value) {
		return map.get(value);
	}
}
