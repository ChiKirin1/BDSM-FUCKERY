package com.zakyfaa.dungeondevices.furniture;

import net.minecraft.util.StringIdentifiable;

/** Head/foot part of a two-block-long piece (was `k`). */
public enum DoublePart implements StringIdentifiable {
	HEAD("head"),
	FOOT("foot");

	private final String name;

	DoublePart(String name) { this.name = name; }

	@Override
	public String asString() { return name; }
}
