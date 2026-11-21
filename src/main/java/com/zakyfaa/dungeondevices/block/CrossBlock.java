package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class CrossBlock extends TallFurnitureBlock {
	public static final MapCodec<CrossBlock> CODEC = createCodec(CrossBlock::new);

	public CrossBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<CrossBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(-2.0, 0.0, 14.0, 18.0, 16.0, 16.0)
			.build();
		this.shapesByHalf = ShapeUtil.rotationsByHalf(s1, s1); // lower, upper
	}
}
