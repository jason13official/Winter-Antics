package com.cursee.winter_antics.impl.common.block.entity;

import com.cursee.winter_antics.impl.common.registry.WABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

public class BlizzardBlockEntity extends BlockEntity {

  public BlizzardBlockEntity(BlockPos pos, BlockState blockState) {
    super(WABlockEntities.BLIZZARD, pos, blockState);
  }

  public static void serverTick(ServerLevel level, BlockPos selfPos, BlockState selfState, BlizzardBlockEntity blockEntity) {

    if (level.getGameTime() % 10 != 0) {
      return;
    }

    int maxSnowLayers = level.getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);

    int y = selfPos.getY();

    int radius = 15;
    int xStart = selfPos.getZ();
    int zStart = selfPos.getZ();

    for (int x = xStart - radius; x <= xStart + radius; x++) {
      for (int z = zStart - radius; z <= zStart + radius; z++) {

        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y - 1, z, 0, 0, 0);

        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0); // spawn in radius around block at same Y level
        if (level.getRandom().nextFloat() <= 0.01) {
          tryPlaceSnow(x, y, z, level, maxSnowLayers);
        }

        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y + 1, z, 0, 0, 0);
      }
    }
  }

  private static void tryPlaceSnow(int x, int y, int z, ServerLevel serverLevel, int maxSnowLayers) {
    var newPos = BlockPos.of(BlockPos.asLong(x, y, z));
    var state = serverLevel.getBlockState(newPos);

    if ((state.isAir() || state.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(serverLevel, newPos)) {
      if (state.is(Blocks.SNOW)) {
        int j = state.getValue(SnowLayerBlock.LAYERS);
        if (j < Math.min(maxSnowLayers, 8)) {
          BlockState newState = state.setValue(SnowLayerBlock.LAYERS, j + 1);
          Block.pushEntitiesUp(state, newState, serverLevel, newPos);
          serverLevel.setBlockAndUpdate(newPos, newState);
          BlockEntity.setChanged(serverLevel, newPos, newState);
        }
      } else {
        serverLevel.setBlockAndUpdate(newPos, Blocks.SNOW.defaultBlockState());
        BlockEntity.setChanged(serverLevel, newPos, state);
      }
    }
  }

  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BlizzardBlockEntity(pos, state);
  }
}
