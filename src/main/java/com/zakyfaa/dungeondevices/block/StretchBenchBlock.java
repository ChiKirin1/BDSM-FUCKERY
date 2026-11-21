package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class StretchBenchBlock extends TripleFurnitureBlock {
	public static final MapCodec<StretchBenchBlock> CODEC = createCodec(StretchBenchBlock::new);

	public StretchBenchBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<StretchBenchBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(0.0, 10.0, 2.0, 16.0, 12.0, 16.0)
			.box(2.0, 14.0, 4.0, 14.0, 16.0, 6.0)
			.box(14.0, 12.0, 2.0, 16.0, 18.0, 8.0)
			.box(0.0, 12.0, 2.0, 2.0, 18.0, 8.0)
			.box(2.0, 0.0, 4.0, 4.0, 10.0, 6.0)
			.box(12.0, 0.0, 4.0, 14.0, 10.0, 6.0)
			.build();
		VoxelShape s2 = new ShapeUtil.Builder()
			.box(0.0, 10.0, 0.0, 16.0, 12.0, 16.0)
			.build();
		VoxelShape s3 = new ShapeUtil.Builder()
			.box(0.0, 10.0, 0.0, 16.0, 12.0, 14.0)
			.box(2.0, 0.0, 10.0, 4.0, 10.0, 12.0)
			.box(12.0, 0.0, 10.0, 14.0, 10.0, 12.0)
			.box(14.0, 12.0, 10.0, 16.0, 16.0, 12.0)
			.box(0.0, 12.0, 10.0, 2.0, 16.0, 12.0)
			.box(6.0, 12.0, 10.0, 10.0, 16.0, 12.0)
			.box(0.0, 16.0, 10.0, 16.0, 18.0, 12.0)
			.build();
		this.shapesByPart = ShapeUtil.rotations(s1, s2, s3); // head, center, foot
	}
}
