package com.maldloader.api.base.event;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Base class for Event implementations.
 * <p>
 * Example Usage:<pre> {@code Event<Runnable> objectEvent = new Event<>(runnables -> () -> {
 * 	for (Runnable runnable : runnables) {
 * 		runnable.run();
 *     }
 * }, Runnable[]::new);
 * } </pre>
 */
public class Event<T> {
	final Function<T[], T> combiner;
	final IntFunction<T[]> create;
	T[] listeners;
	T listener;

	public Event(Function<T[], T> combiner, IntFunction<T[]> create) {
		this.combiner = combiner;
		this.create = create;
		this.listeners = create.apply(0);
		this.recompile();
	}

	void recompile() {
		this.listener = this.combiner.apply(this.listeners);
	}

	public void add(T listener) {
		T[] old = this.listeners;
		T[] _new = this.listeners = Arrays.copyOf(old, old.length + 1);
		_new[old.length] = listener;
		this.recompile();
	}

	public T getInvoker() {
		return this.listener;
	}
}