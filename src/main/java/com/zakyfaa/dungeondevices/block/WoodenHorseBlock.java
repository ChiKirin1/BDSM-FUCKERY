package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class WoodenHorseBlock extends FurnitureBlock {
	public static final MapCodec<WoodenHorseBlock> CODEC = createCodec(WoodenHorseBlock::new);

	public WoodenHorseBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<WoodenHorseBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(4.0, 0.0, 11.25, 12.0, 1.15, 13.75)
			.box(7.0, 1.15, 11.65, 9.0, 9.15, 13.4)
			.box(4.0, 0.0, 2.25, 12.0, 1.15, 4.75)
			.box(7.0, 1.15, 2.65, 9.0, 9.15, 4.4)
			.box(4.725, 9.2, 0.0, 11.225, 9.7, 16.0)
			.build();
		this.shapes = ShapeUtil.rotations(s1);
	}
}
