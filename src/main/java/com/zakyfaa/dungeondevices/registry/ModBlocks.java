package com.zakyfaa.dungeondevices.registry;

import com.zakyfaa.dungeondevices.DungeonDevices;
import com.zakyfaa.dungeondevices.block.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/** All blocks (was `ap`, `B`, `D`). Block items are registered alongside each block. */
public final class ModBlocks {
	/** Items for the "Dungeon Devices" creative tab / the "Blocks" tab. */
	public static final List<Block> FURNITURE_TAB = new ArrayList<>();
	public static final List<Block> BLOCK_TAB = new ArrayList<>();

	// ---------- furniture ----------
	public static final BondageBenchBlock BONDAGE_BENCH = furniture("bondage_bench", BondageBenchBlock::new, ModBlocks::wood);
	public static final StretchBenchBlock STRETCH_BENCH = furniture("stretch_bench", StretchBenchBlock::new, ModBlocks::wood);
	public static final WoodenHorseBlock WOODEN_HORSE = furniture("wooden_horse", WoodenHorseBlock::new, ModBlocks::wood);
	public static final WeightedWoodenHorseBlock WEIGHTED_WOODEN_HORSE = furniture("weighted_wooden_horse", WeightedWoodenHorseBlock::new, ModBlocks::wood);
	public static final PilloryBlock PILLORY = furniture("pillory", PilloryBlock::new, ModBlocks::wood);
	public static final CrossBlock CROSS = furniture("cross", CrossBlock::new, ModBlocks::wood);
	public static final CageBlock CAGE = furniture("cage", CageBlock::new, ModBlocks::metal);
	public static final CuffBedBlock CUFF_BED = furniture("cuff_bed", CuffBedBlock::new, ModBlocks::wood);
	public static final BondagePoleBlock BONDAGE_POLE = furniture("bondage_pole", BondagePoleBlock::new, ModBlocks::wood);
	public static final WallCuffBlock WALL_CUFF = furniture("wall_cuff", WallCuffBlock::new, ModBlocks::wood);

	// ---------- doors ----------
	public static final Block DUNGEON_DOOR = building("dungeon_door",
		s -> new DoorBlock(BlockSetType.OAK, s),
		() -> AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).strength(3.0F).sounds(BlockSoundGroup.WOOD).nonOpaque());
	public static final Block DUNGEON_TRAPDOOR = building("dungeon_trapdoor",
		s -> new TrapdoorBlock(BlockSetType.OAK, s),
		() -> AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).strength(3.0F).sounds(BlockSoundGroup.WOOD).nonOpaque().allowsSpawning((st, w, p, t) -> false));
	public static final Block IRON_BARS_DOOR = building("iron_bars_door",
		s -> new DoorBlock(BlockSetType.IRON, s),
		() -> AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(5.0F).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque());
	public static final Block IRON_BARS_TRAPDOOR = building("iron_bars_trapdoor",
		s -> new TrapdoorBlock(BlockSetType.IRON, s),
		() -> AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(5.0F).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning((st, w, p, t) -> false));

	// ---------- building block sets ----------
	public enum Variant { BASE, PILLAR, STAIRS, SLAB, WALL, FENCE, FENCE_GATE, CRACKED, POLISHED, CHISELED, BRICKS, MOSSY }

	public static final EnumSet<Variant> NATURAL_STONE_SET = EnumSet.of(Variant.BASE, Variant.STAIRS, Variant.SLAB, Variant.WALL);
	public static final EnumSet<Variant> NATURAL_WOOD_SET = EnumSet.of(Variant.BASE, Variant.STAIRS, Variant.SLAB, Variant.FENCE, Variant.FENCE_GATE);

	public static final List<BlockSet> ALL_SETS = new ArrayList<>();

	public static final BlockSet DUNGEON_PLANKS = set("dungeon_planks", true, NATURAL_WOOD_SET);
	public static final BlockSet DUNGEON_BRICKS = set("dungeon_bricks", false,
		EnumSet.of(Variant.BASE, Variant.CHISELED, Variant.CRACKED, Variant.PILLAR, Variant.STAIRS, Variant.SLAB, Variant.WALL));
	public static final BlockSet POLISHED_DUNGEON_BRICKS = set("polished_dungeon_bricks", false, NATURAL_STONE_SET);
	public static final BlockSet MOSSY_DUNGEON_BRICKS = set("mossy_dungeon_bricks", false, NATURAL_STONE_SET);
	public static final BlockSet DUNGEON_TILES = set("dungeon_bricks_tiles", false, NATURAL_STONE_SET);

	/** One family of related blocks (was `B`). */
	public static final class BlockSet {
		public final String baseName;
		public final boolean wood;
		public final Map<Variant, Block> blocks = new EnumMap<>(Variant.class);

		BlockSet(String baseName, boolean wood) {
			this.baseName = baseName;
			this.wood = wood;
		}

		public Block get(Variant v) { return blocks.get(v); }
	}

	private static BlockSet set(String base, boolean wood, EnumSet<Variant> variants) {
		BlockSet set = new BlockSet(base, wood);
		Supplier<AbstractBlock.Settings> settings = wood
			? () -> AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)
			: () -> AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(2.0F, 6.0F).requiresTool();
		// BASE first, stairs need its default state.
		for (Variant v : Variant.values()) {
			if (!variants.contains(v)) continue;
			Function<AbstractBlock.Settings, Block> factory = switch (v) {
				case BASE, CRACKED, POLISHED, CHISELED, BRICKS, MOSSY -> Block::new;
				case PILLAR -> PillarBlock::new;
				case STAIRS -> s -> new StairsBlock(set.get(Variant.BASE).getDefaultState(), s);
				case SLAB -> SlabBlock::new;
				case WALL -> WallBlock::new;
				case FENCE -> FenceBlock::new;
				case FENCE_GATE -> s -> new FenceGateBlock(WoodType.OAK, s);
			};
			String name = switch (v) {
				case BASE -> base;
				case PILLAR -> base + "_pillar";
				case STAIRS -> base + "_stairs";
				case SLAB -> base + "_slab";
				case WALL -> base + "_wall";
				case FENCE -> base + "_fence";
				case FENCE_GATE -> base + "_fence_gate";
				case POLISHED -> "polished_" + base;
				case CRACKED -> "cracked_" + base;
				case CHISELED -> "chiseled_" + base;
				case BRICKS -> base + "_bricks";
				case MOSSY -> "mossy_" + base;
			};
			set.blocks.put(v, building(name, factory, settings));
		}
		ALL_SETS.add(set);
		return set;
	}

	// ---------- helpers ----------
	private static AbstractBlock.Settings wood() {
		return AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).strength(2.0F, 3.0F)
			.sounds(BlockSoundGroup.WOOD).nonOpaque().pistonBehavior(PistonBehavior.DESTROY);
	}

	private static AbstractBlock.Settings metal() {
		return AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(2.0F, 3.0F)
			.sounds(BlockSoundGroup.METAL).nonOpaque().pistonBehavior(PistonBehavior.DESTROY);
	}

	private static <T extends Block> T furniture(String name, Function<AbstractBlock.Settings, T> factory, Supplier<AbstractBlock.Settings> settings) {
		T block = registerBlock(name, factory, settings.get());
		FURNITURE_TAB.add(block);
		return block;
	}

	private static <T extends Block> T building(String name, Function<AbstractBlock.Settings, T> factory, Supplier<AbstractBlock.Settings> settings) {
		T block = registerBlock(name, factory, settings.get());
		BLOCK_TAB.add(block);
		return block;
	}

	/** 1.21.2+: block and item settings must carry their registry key before construction. */
	private static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, DungeonDevices.id(name));
		T block = Registry.register(Registries.BLOCK, blockKey, factory.apply(settings.registryKey(blockKey)));

		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, DungeonDevices.id(name));
		Registry.register(Registries.ITEM, itemKey,
			new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey()));
		return block;
	}

	/** The ten pieces of furniture that can seat a player. */
	public static List<Block> furnitureBlocks() {
		return List.of(BONDAGE_BENCH, STRETCH_BENCH, CROSS, CAGE, PILLORY, CUFF_BED, BONDAGE_POLE,
			WOODEN_HORSE, WEIGHTED_WOODEN_HORSE, WALL_CUFF);
	}

	public static void init() {
		// Loading this class registers everything above.
	}
}
