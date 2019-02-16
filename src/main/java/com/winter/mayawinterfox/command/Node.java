package com.winter.mayawinterfox.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Node<T> {

	private final T data;
	private final List<Node<T>> children;
	private Node<T> parent;

	public Node(T data, List<Node<T>> children) {
		this.data = data;
		this.children = children;
		children.forEach(c -> c.setParent(this));
	}

	public T getData() {
		return data;
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	private void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getParent() {
		return parent;
	}

	public <U> U compileTopDown(Function<T, U> converter, BinaryOperator<U> concatenator) {
		List<Node<T>> me = new LinkedList<>();
		me.add(this);
		Node<T> current = this;
		while (current.getParent() != null) {
			me.add(0, current.getParent());
			current = current.getParent();
		}
		return me.stream().map(t -> converter.apply(t.getData())).reduce(concatenator).orElse(converter.apply(this.getData()));
	}

	public <U> Node<T> traverseThis(Function<Node<T>, Set<U>> converter, U toMatch, BiPredicate<U, U> equalityCheck, boolean unused) {
		for (U thing : converter.apply(this)) {
			for (Node<T> child : getChildren()) {
				for (U childThing : converter.apply(child)) {
					if (equalityCheck.test(childThing, toMatch)) {
						return child.traverseThis(converter, toMatch, equalityCheck, unused);
					}
				}
			}
			if (equalityCheck.test(thing, toMatch)) {
				return this;
			}
		}
		return null;
	}

}
