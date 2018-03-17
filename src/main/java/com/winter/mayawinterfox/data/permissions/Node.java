package com.winter.mayawinterfox.data.permissions;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Node implements Predicate<Node> {

	private final String node;

	public Node(String node) {
		this.node = node;
	}

	public String getNode() {
		return node;
	}

	@Override
	public boolean test(Node node) {
		if (this.node.equals("*"))
			return true;
		String text = Arrays.stream(this.node.split("(?:^\\*(\\.))|(?:(?<=\\.)\\*(?=\\.))|(?:(?<=\\.)\\*$)"))
				.map(Pattern::quote)
				.collect(Collectors.joining(".+")) + (this.node.endsWith("*") ? ".+" : "");

		return node.getNode().matches(text);
	}
}
