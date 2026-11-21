package com.zakyfaa.dungeondevices.block;

import com.mojang.serialization.MapCodec;
import com.zakyfaa.dungeondevices.util.ShapeUtil;
import net.minecraft.util.shape.VoxelShape;

public class CuffBedBlock extends BedFurnitureBlock {
	public static final MapCodec<CuffBedBlock> CODEC = createCodec(CuffBedBlock::new);

	public CuffBedBlock(Settings settings) {
		super(settings);
		initShapes();
	}

	@Override
	protected MapCodec<CuffBedBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void initShapes() {
		VoxelShape s1 = new ShapeUtil.Builder()
			.box(0.0, 3.0, 0.0, 16.0, 9.0, 16.0)
			.box(13.0, 0.0, 0.0, 16.0, 3.0, 3.0)
			.box(0.0, 0.0, 0.0, 3.0, 3.0, 3.0)
			.build();
		VoxelShape s2 = new ShapeUtil.Builder()
			.box(0.0, 3.0, 0.0, 16.0, 9.0, 16.0)
			.box(0.0, 0.0, 13.0, 3.0, 3.0, 16.0)
			.box(13.0, 0.0, 13.0, 16.0, 3.0, 16.0)
			.build();
		this.shapesByPart = ShapeUtil.rotations(s1, s2); // head, foot
	}
}
