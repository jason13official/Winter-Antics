package com.cursee.winter_antics.impl.common.block;

import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class WallOrnamentBlock extends OrnamentBlock {

  public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
  public static final MapCodec<WallOrnamentBlock> CODEC = simpleCodec(WallOrnamentBlock::new);

  private static final VoxelShape UNDERLYING_SHAPE = Block.column(8, 3, 12);

  private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(UNDERLYING_SHAPE.move(0.0f, 0.0f, 0.1875f).optimize());

  public WallOrnamentBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(MAP_COLOR, MapColor.COLOR_RED.id).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
  }

  public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
    BlockPos blockpos = pos.relative(facing.getOpposite());
    BlockState blockstate = level.getBlockState(blockpos);
    return blockstate.isFaceSturdy(level, blockpos, facing);
  }

  @Override
  protected MapCodec<? extends OrnamentBlock> codec() {
    return CODEC;
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return canSurvive(level, pos, state.getValue(FACING));
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPES.get(state.getValue(FACING));
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState blockstate = this.defaultBlockState();
    LevelReader levelreader = context.getLevel();
    BlockPos blockpos = context.getClickedPos();

    for (Direction direction : context.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        blockstate = blockstate.setValue(FACING, direction).setValue(MAP_COLOR, context.getLevel().getRandom().nextInt(0, 62));
        if (blockstate.canSurvive(levelreader, blockpos)) {
          return blockstate;
        }
      }
    }

    return null;
  }

  protected BlockState updateShape(BlockState p_51771_, LevelReader p_374569_, ScheduledTickAccess p_374118_, BlockPos p_51775_, Direction p_51772_, BlockPos p_51776_, BlockState p_51773_,
      RandomSource p_374448_) {
    return p_51772_ == p_51771_.getValue(FACING) && !p_51771_.canSurvive(p_374569_, p_51775_) ? Blocks.AIR.defaultBlockState()
        : super.updateShape(p_51771_, p_374569_, p_374118_, p_51775_, p_51772_, p_51776_, p_51773_, p_374448_);
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(MAP_COLOR, WATERLOGGED, FACING);
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
    return false;
  }

  @Override
  protected void attack(BlockState state, Level level, BlockPos pos, Player player) {

    if (!(level instanceof ServerLevel serverLevel)) {
      return;
    }

    serverLevel.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);

    // serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    level.destroyBlock(pos, false);

    player.hurtServer(serverLevel, serverLevel.damageSources().cactus(), 1.0f);
  }
}
