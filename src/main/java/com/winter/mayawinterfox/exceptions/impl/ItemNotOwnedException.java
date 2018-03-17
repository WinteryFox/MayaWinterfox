package com.winter.mayawinterfox.exceptions.impl;

public class ItemNotOwnedException extends RuntimeException {

	public ItemNotOwnedException() {
		super("You don't own that item.");
	}

	public ItemNotOwnedException(String meta) {
		super(meta);
	}
}
