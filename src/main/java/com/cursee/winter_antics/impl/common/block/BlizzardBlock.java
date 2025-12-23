package com.cursee.winter_antics.impl.common.block;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.client.WinterAnticsClient;
import com.cursee.winter_antics.impl.common.config.WAConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import oshi.util.tuples.Pair;

public class BlizzardBlock extends Block {

  public BlizzardBlock(Properties p_49795_) {
    super(p_49795_);
  }

//  @Override
//  public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
//    super.setPlacedBy(level, pos, state, placer, stack);
//
//    if (level.isClientSide()) {
//
//      if (WAConfig.DEBUGGING.getAsBoolean()) {
//        WinterAntics.LOG.info("New Blizzard position {}", pos.toShortString());
//      }
//
//      WinterAnticsClient.BLIZZARD_POSITIONS.add(new Pair<>(pos.getX(), pos.getZ()));
//    }
//  }
//
//  @Override
//  public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
//    super.onBlockStateChange(level, pos, oldState, newState);
//
//
//    if (!(newState.getBlock() instanceof BlizzardBlock) && level.isClientSide()) {
//      if (WAConfig.DEBUGGING.getAsBoolean()) {
//        WinterAntics.LOG.info("Removing Blizzard position {}", pos.toShortString());
//      }
//      WinterAnticsClient.BLIZZARD_POSITIONS.remove(new Pair<>(pos.getX(), pos.getZ()));
//    }
//  }
}
