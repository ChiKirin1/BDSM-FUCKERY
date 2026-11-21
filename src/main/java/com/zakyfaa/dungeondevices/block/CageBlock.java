package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class CageBlock extends TallFurnitureBlock {
	public static final MapCodec<CageBlock> CODEC = createCodec(CageBlock::new);

	public CageBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<CageBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
			.box(0.0, 0.0, 15.0, 16.0, 15.0, 16.0)
			.box(0.0, 0.0, 1.0, 1.0, 15.0, 15.0)
			.box(15.0, 0.0, 1.0, 16.0, 15.0, 15.0)
			.box(0.0, 0.0, 0.0, 16.0, 8.0, 1.0)
			.box(0.0, 12.0, 0.0, 16.0, 15.0, 1.0)
			.box(12.0, 4.0, 0.0, 16.0, 12.0, 1.0)
			.box(0.0, 4.0, 0.0, 4.0, 12.0, 1.0)
			.build();
		VoxelShape s2 = new ShapeUtil.Builder()
			.box(0.0, 1.0, 0.0, 16.0, 16.0, 1.0)
			.box(0.0, 1.0, 15.0, 16.0, 16.0, 16.0)
			.box(15.0, 1.0, 1.0, 16.0, 16.0, 15.0)
			.box(0.0, 1.0, 1.0, 1.0, 16.0, 15.0)
			.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
			.build();
		this.shapesByHalf = ShapeUtil.rotationsByHalf(s2, s1); // lower, upper
	}
}
