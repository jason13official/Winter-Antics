package com.cursee.winter_antics.impl.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class OrnamentBlock extends Block implements SimpleWaterloggedBlock {

//  public static final Codec<MapColor> MAP_COLOR_CODEC = intResolver(OrnamentBlock::colorToInt, OrnamentBlock::intToColor);
//
//  private static MapColor intToColor(Integer value) {
//    return MapColor.byId(value);
//  }
//
//  private static Integer colorToInt(MapColor mapColor) {
//    return mapColor.id;
//  }
//
//  static <E> Codec<E> intResolver(final Function<E, Integer> toInt, final Function<Integer, E> fromInt) {
//    return Codec.INT.flatXmap(
//        name -> Optional.ofNullable(fromInt.apply(name)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + name)),
//        e -> Optional.ofNullable(toInt.apply(e)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + e))
//    );
//  }

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final IntegerProperty MAP_COLOR = IntegerProperty.create("map_color", 0, 61); // 61 inclusive

  public OrnamentBlock(Properties properties) {
    super(properties);
  }

  protected abstract MapCodec<? extends OrnamentBlock> codec();

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(MAP_COLOR);
  }

  @Override
  public boolean isPossibleToRespawnInThis(BlockState state) {
    return false; // prevent spawning on ornaments
  }

  protected BlockState updateShape(BlockState p_56285_, LevelReader p_374509_, ScheduledTickAccess p_374520_, BlockPos p_56289_, Direction p_56286_, BlockPos p_56290_, BlockState p_56287_,
      RandomSource p_374213_) {
    if (p_56285_.getValue(WATERLOGGED)) {
      p_374520_.scheduleTick(p_56289_, Fluids.WATER, Fluids.WATER.getTickDelay(p_374509_));
    }

    return super.updateShape(p_56285_, p_374509_, p_374520_, p_56289_, p_56286_, p_56290_, p_56287_, p_374213_);
  }

  protected FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }
}
