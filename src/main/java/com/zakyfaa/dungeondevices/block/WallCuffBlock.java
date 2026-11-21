package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class WallCuffBlock extends FurnitureBlock {
	public static final MapCodec<WallCuffBlock> CODEC = createCodec(WallCuffBlock::new);

	public WallCuffBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<WallCuffBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(-5.0, 6.5, 14.4, 21.0, 15.5, 16.0)
			.build();
		this.shapes = ShapeUtil.rotations(s1);
	}
}
