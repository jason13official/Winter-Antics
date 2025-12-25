package com.cursee.winter_antics.impl.common.block;

import com.cursee.winter_antics.impl.common.block.entity.BlizzardBlockEntity;
import com.cursee.winter_antics.impl.common.registry.WABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlizzardBlock extends Block implements EntityBlock {

  public BlizzardBlock(Properties p_49795_) {
    super(p_49795_);
  }

  protected static <T extends BlockEntity> BlockEntityTicker<T> createBlizzardTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends BlizzardBlockEntity> clientType) {

    if (!(level instanceof ServerLevel serverLevel)) {
      return null;
    }

    return createTickerHelper(serverType, clientType, (aLevel, pos, state, entityType) -> BlizzardBlockEntity.serverTick(serverLevel, pos, state, entityType));

  }

  @SuppressWarnings("all")
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actualType, BlockEntityType<E> expectedType,
      BlockEntityTicker<? super E> ticker) {
    return expectedType == actualType ? (BlockEntityTicker<A>) ticker : null;
  }

  public BlockEntity newBlockEntity(BlockPos p_153277_, BlockState p_153278_) {
    return new BlizzardBlockEntity(p_153277_, p_153278_);
  }

  @Override
  public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level p_153273_, BlockState p_153274_, BlockEntityType<T> p_153275_) {
    return createBlizzardTicker(p_153273_, p_153275_, WABlockEntities.BLIZZARD);
  }

  @Override
  public void animateTick(BlockState selfState, Level level, BlockPos selfPos, RandomSource random) {

    int y = selfPos.getY();

    int radius = 15;
    int xStart = selfPos.getZ();
    int zStart = selfPos.getZ();

    for (int x = xStart - radius; x <= xStart + radius; x++) {
      for (int z = zStart - radius; z <= zStart + radius; z++) {

        // lol
//        level.addParticle(ParticleTypes.SNOWFLAKE, x, y - 2, z, 0, 0, 0); // spawn in radius around block at same Y level
//        level.addParticle(ParticleTypes.SNOWFLAKE, x, y - 1, z, 0, 0, 0); // spawn in radius around block at same Y level

//        level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0); // spawn in radius around block at same Y level
//
        level.addParticle(ParticleTypes.SNOWFLAKE, x, y + 1, z, 0, 0, 0); // spawn in radius around block at same Y level
        level.addParticle(ParticleTypes.SNOWFLAKE, x, y + 2, z, 0, 0, 0); // spawn in radius around block at same Y level
      }
    }
  }

  // IGNORE

//  private void tryPlaceSnow() {
//    int i = this.getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);
//    if (i > 0 && biome.shouldSnow(this, blockpos)) {
//      BlockState blockstate = this.getBlockState(blockpos);
//      if (blockstate.is(Blocks.SNOW)) {
//        int j = blockstate.getValue(SnowLayerBlock.LAYERS);
//        if (j < Math.min(i, 8)) {
//          BlockState blockstate1 = blockstate.setValue(SnowLayerBlock.LAYERS, j + 1);
//          Block.pushEntitiesUp(blockstate, blockstate1, this, blockpos);
//          this.setBlockAndUpdate(blockpos, blockstate1);
//        }
//      } else {
//        this.setBlockAndUpdate(blockpos, Blocks.SNOW.defaultBlockState());
//      }
//    }
//  }

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
