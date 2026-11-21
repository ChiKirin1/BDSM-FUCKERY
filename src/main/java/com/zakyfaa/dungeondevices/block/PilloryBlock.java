package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class PilloryBlock extends FurnitureBlock {
	public static final MapCodec<PilloryBlock> CODEC = createCodec(PilloryBlock::new);

	public PilloryBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<PilloryBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(1.0, 0.0, 5.0, 15.0, 1.0, 11.0)
			.box(4.975, 1.0, 7.25, 10.975, 10.0, 8.75)
			.box(-2.0, 10.0, 7.0, 18.0, 12.0, 9.0)
			.box(-2.0, 18.0, 7.0, 18.0, 20.0, 9.0)
			.box(16.0, 12.0, 7.0, 20.0, 18.0, 9.0)
			.box(-4.0, 12.0, 7.0, 0.0, 18.0, 9.0)
			.box(11.0, 12.0, 7.0, 13.0, 18.0, 9.0)
			.box(3.0, 12.0, 7.0, 5.0, 18.0, 9.0)
			.box(13.0, 17.0, 7.0, 16.0, 18.0, 9.0)
			.box(13.0, 12.0, 7.0, 16.0, 13.0, 9.0)
			.box(0.0, 17.0, 7.0, 3.0, 18.0, 9.0)
			.box(0.0, 12.0, 7.0, 3.0, 13.0, 9.0)
			.build();
		this.shapes = ShapeUtil.rotations(s1);
	}
}
