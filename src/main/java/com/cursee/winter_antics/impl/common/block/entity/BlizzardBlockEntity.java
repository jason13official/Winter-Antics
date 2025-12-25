package com.cursee.winter_antics.impl.common.block.entity;

import com.cursee.winter_antics.impl.common.registry.WABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
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
    int xStart = selfPos.getX();
    int zStart = selfPos.getZ();

    MutableBlockPos mutable = new MutableBlockPos();

    for (int x = xStart - radius; x <= xStart + radius; x++) {
      for (int z = zStart - radius; z <= zStart + radius; z++) {


        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y - 1, z, 0, 0, 0);

        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0); // spawn in radius around block at same Y level
        if (level.getRandom().nextFloat() <= 0.01) {
          mutable.set(x, y, z);
          tryPlaceSnow(mutable.below(), level, maxSnowLayers);
          tryPlaceSnow(mutable, level, maxSnowLayers);
          tryPlaceSnow(mutable.above(), level, maxSnowLayers);
        }

        // level.addParticle(ParticleTypes.SNOWFLAKE, x, y + 1, z, 0, 0, 0);
      }
    }
  }

  private static void tryPlaceSnow(BlockPos pos, ServerLevel serverLevel, int maxSnowLayers) {

    var state = serverLevel.getBlockState(pos);

    if ((state.isAir() || state.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(serverLevel, pos)) {
      if (state.is(Blocks.SNOW)) {
        int j = state.getValue(SnowLayerBlock.LAYERS);
        if (j < Math.min(maxSnowLayers, 8)) {
          BlockState newState = state.setValue(SnowLayerBlock.LAYERS, j + 1);
          Block.pushEntitiesUp(state, newState, serverLevel, pos);
          serverLevel.setBlockAndUpdate(pos, newState);
          BlockEntity.setChanged(serverLevel, pos, newState);
        }
      } else {
        serverLevel.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
        BlockEntity.setChanged(serverLevel, pos, state);
      }
    }
  }

  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BlizzardBlockEntity(pos, state);
  }
}
