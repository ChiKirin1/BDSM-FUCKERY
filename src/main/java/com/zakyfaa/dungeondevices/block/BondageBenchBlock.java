package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class BondageBenchBlock extends FurnitureBlock {
	public static final MapCodec<BondageBenchBlock> CODEC = createCodec(BondageBenchBlock::new);

	public BondageBenchBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<BondageBenchBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(2.0, 0.0, 0.0, 4.0, 6.0, 2.0)
			.box(12.0, 0.0, 0.0, 14.0, 6.0, 2.0)
			.box(2.0, 0.0, 14.0, 4.0, 6.0, 16.0)
			.box(12.0, 0.0, 14.0, 14.0, 6.0, 16.0)
			.box(2.0, 6.0, -4.0, 4.0, 8.0, 19.0)
			.box(12.0, 6.0, -4.0, 14.0, 8.0, 19.0)
			.box(4.28, 12.98, 0.92, 11.72, 14.16, 11.54)
			.box(10.75, 8.0, 14.5, 15.25, 9.0, 19.5)
			.box(0.75, 8.0, 14.5, 5.25, 9.0, 19.5)
			.box(-1.75, 9.0, -4.5, 2.75, 10.0, 0.5)
			.box(13.25, 9.0, -4.5, 17.75, 10.0, 0.5)
			.build();
		this.shapes = ShapeUtil.rotations(s1);
	}
}
