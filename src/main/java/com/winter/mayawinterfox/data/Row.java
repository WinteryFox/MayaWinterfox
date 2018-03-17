package com.winter.mayawinterfox.data;

import java.util.HashMap;

public class Row {

	private final HashMap<String, Object> columns = new HashMap<>();

	public HashMap<String, Object> getColumns() {
		return columns;
	}

	public Object get(String key) {
		return columns.get(key);
	}

	public boolean containsKey(String key) {
		return columns.containsKey(key);
	}

	public void addColumn(String key, Object data) {
		columns.put(key, data);
	}

	public void removeColumn(String key) {
		columns.remove(key);
	}
}
