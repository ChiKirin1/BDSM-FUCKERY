package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class BondagePoleBlock extends TallFurnitureBlock {
	public static final MapCodec<BondagePoleBlock> CODEC = createCodec(BondagePoleBlock::new);

	public BondagePoleBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<BondagePoleBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
			.box(1.0, 11.5, 10.0, 15.0, 13.0, 11.0)
			.box(1.0, 7.0, 10.0, 15.0, 8.5, 11.0)
			.box(5.5, 8.5, 10.0, 10.5, 11.5, 11.0)
			.box(1.0, 8.5, 10.0, 2.5, 11.5, 11.0)
			.box(13.5, 8.5, 10.0, 15.0, 11.5, 11.0)
			.build();
		VoxelShape s2 = new ShapeUtil.Builder()
			.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
			.box(1.0, 6.5, 10.0, 15.0, 8.0, 11.0)
			.box(1.0, 2.0, 10.0, 15.0, 3.5, 11.0)
			.box(5.5, 3.5, 10.0, 10.5, 6.5, 11.0)
			.box(1.0, 3.5, 10.0, 2.5, 6.5, 11.0)
			.box(13.5, 3.5, 10.0, 15.0, 6.5, 11.0)
			.build();
		this.shapesByHalf = ShapeUtil.rotationsByHalf(s2, s1); // lower, upper
	}
}
