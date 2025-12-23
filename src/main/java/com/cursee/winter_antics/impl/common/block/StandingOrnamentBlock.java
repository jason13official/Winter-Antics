package com.cursee.winter_antics.impl.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StandingOrnamentBlock extends OrnamentBlock {

  public static final MapCodec<StandingOrnamentBlock> CODEC = simpleCodec(StandingOrnamentBlock::new);

  // private static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 4, 10);
  private static final VoxelShape SHAPE = Block.box(3.0F, 0.0F, 3.0F, 12.0F, 7.0F, 12.0F);

  public StandingOrnamentBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(MAP_COLOR, MapColor.COLOR_RED.id).setValue(WATERLOGGED, false));
  }

  @Override
  protected MapCodec<? extends OrnamentBlock> codec() {
    return CODEC;
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(MAP_COLOR, WATERLOGGED);
  }

  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return level.getBlockState(pos.below()).isSolid();
  }

  public BlockState getStateForPlacement(BlockPlaceContext context) {
    FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
    return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
  }

  protected BlockState updateShape(BlockState p_57005_, LevelReader p_374220_, ScheduledTickAccess p_374377_, BlockPos p_57009_, Direction p_57006_, BlockPos p_57010_, BlockState p_57007_,
      RandomSource p_374251_) {
    return p_57006_ == Direction.DOWN && !this.canSurvive(p_57005_, p_374220_, p_57009_) ? Blocks.AIR.defaultBlockState()
        : super.updateShape(p_57005_, p_374220_, p_374377_, p_57009_, p_57006_, p_57010_, p_57007_, p_374251_);
  }

  @Override
  protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean intersects) {

    if (!(level instanceof ServerLevel serverLevel)) {
      return;
    }

    if (!(entity instanceof LivingEntity living)) {
      return;
    }

    serverLevel.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);

    // serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    level.destroyBlock(pos, false);

    living.hurtServer(serverLevel, serverLevel.damageSources().cactus(), 1.0f);

  }
}
