package com.zakyfaa.dungeondevices.furniture;

import net.minecraft.util.StringIdentifiable;

/** Head/centre/foot part of a three-block-long piece (was `l`). */
public enum TriplePart implements StringIdentifiable {
	HEAD("head"),
	CENTER("center"),
	FOOT("foot");

	private final String name;

	TriplePart(String name) { this.name = name; }

	@Override
	public String asString() { return name; }
}
