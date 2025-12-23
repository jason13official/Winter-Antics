package com.cursee.winter_antics.impl.common.block;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.client.WinterAnticsClient;
import com.cursee.winter_antics.impl.common.config.WAConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

public class BlizzardBlock extends Block {

  public BlizzardBlock(Properties p_49795_) {
    super(p_49795_);
  }

  @Override
  protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("New Blizzard position {}", pos.toShortString());
    }

    if (state.getBlock() instanceof BlizzardBlock && level.isClientSide()) {
      WinterAnticsClient.BLIZZARD_POSITIONS.add(new Pair<>(pos.getX(), pos.getZ()));
    }
  }

  @Override
  public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
    super.onBlockStateChange(level, pos, oldState, newState);

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("Removing Blizzard position {}", pos.toShortString());
    }

    if (!(newState.getBlock() instanceof BlizzardBlock) && level.isClientSide()) {
      WinterAnticsClient.BLIZZARD_POSITIONS.remove(new Pair<>(pos.getX(), pos.getZ()));
    }
  }
}
