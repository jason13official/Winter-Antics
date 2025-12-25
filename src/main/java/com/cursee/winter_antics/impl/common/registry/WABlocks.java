package com.cursee.winter_antics.impl.common.registry;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.common.block.BlizzardBlock;
import com.cursee.winter_antics.impl.common.block.OrnamentBlock;
import com.cursee.winter_antics.impl.common.block.StandingOrnamentBlock;
import com.cursee.winter_antics.impl.common.block.WallOrnamentBlock;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class WABlocks {

  public static Identifier BLIZZARD_ID;
  public static ResourceKey<Block> BLIZZARD_KEY;
  public static Block BLIZZARD;

  public static Identifier ORNAMENT_FLOOR_ID;
  public static ResourceKey<Block> ORNAMENT_FLOOR_KEY;
  public static Block ORNAMENT_FLOOR;

  public static Identifier ORNAMENT_WALL_ID;
  public static ResourceKey<Block> ORNAMENT_WALL_KEY;
  public static Block ORNAMENT_WALL;

  public static Identifier WREATH_ID;
  public static ResourceKey<Block> WREATH_KEY;
  public static Block WREATH;

  public static void register(BiConsumer<Block, Identifier> consumer) {

    BLIZZARD_ID = WinterAntics.identifier("blizzard");
    BLIZZARD_KEY = ResourceKey.create(Registries.BLOCK, BLIZZARD_ID);
    BLIZZARD = new BlizzardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL).setId(BLIZZARD_KEY));

    ORNAMENT_FLOOR_ID = WinterAntics.identifier("ornament");
    ORNAMENT_FLOOR_KEY = ResourceKey.create(Registries.BLOCK, ORNAMENT_FLOOR_ID);
    ORNAMENT_FLOOR = new StandingOrnamentBlock(
        BlockBehaviour.Properties.of()
            .mapColor(state -> MapColor.byId(state.getValue(OrnamentBlock.MAP_COLOR)))
            .forceSolidOn()
            .strength(0.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .setId(ORNAMENT_FLOOR_KEY));

    ORNAMENT_WALL_ID = WinterAntics.identifier("ornament_wall");
    ORNAMENT_WALL_KEY = ResourceKey.create(Registries.BLOCK, ORNAMENT_WALL_ID);
    ORNAMENT_WALL = new WallOrnamentBlock(
        BlockBehaviour.Properties.of()
            .mapColor(state -> MapColor.byId(state.getValue(OrnamentBlock.MAP_COLOR)))
            .strength(0.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .setId(ORNAMENT_WALL_KEY));

    WREATH_ID = WinterAntics.identifier("wreath");
    WREATH_KEY = ResourceKey.create(Registries.BLOCK, WREATH_ID);
    WREATH = new WallOrnamentBlock(
        BlockBehaviour.Properties.of()
            .mapColor(state -> MapColor.byId(state.getValue(OrnamentBlock.MAP_COLOR)))
            .strength(0.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .setId(WREATH_KEY));

    consumer.accept(BLIZZARD, BLIZZARD_ID);
    consumer.accept(ORNAMENT_FLOOR, ORNAMENT_FLOOR_ID);
    consumer.accept(ORNAMENT_WALL, ORNAMENT_WALL_ID);
    consumer.accept(WREATH, WREATH_ID);
  }
}
