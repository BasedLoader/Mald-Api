package com.maldloader.api.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Things that {@link net.minecraft.world.level.block.Block} cannot do alone.
 */
public interface BlockExtensions {

	/**
	 * Called when Minecraft checks if an entity should start, stop or continue burning
	 *
	 * @param state the BlockState of the block
	 * @param level access to the level where the block is
	 * @param pos   the position of the block
	 * @return if the block should burn the entity
	 */
	default boolean canBurn(BlockState state, LevelAccessor level, BlockPos pos) {
		return this == Blocks.FIRE || this == Blocks.LAVA;
	}
}
