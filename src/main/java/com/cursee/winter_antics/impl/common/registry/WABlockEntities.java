package com.cursee.winter_antics.impl.common.registry;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.common.block.entity.BlizzardBlockEntity;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class WABlockEntities {

  public static Identifier BLIZZARD_ID;
  public static ResourceKey<BlockEntityType<?>> BLIZZARD_KEY;
  public static BlockEntityType<BlizzardBlockEntity> BLIZZARD;

  public static void register(BiConsumer<BlockEntityType<?>, Identifier> consumer) {

    BLIZZARD_ID = WinterAntics.identifier("blizzard");
    BLIZZARD_KEY = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, BLIZZARD_ID);
    BLIZZARD = new BlockEntityType<BlizzardBlockEntity>(BlizzardBlockEntity::new, WABlocks.BLIZZARD);

    consumer.accept(BLIZZARD, BLIZZARD_ID);
  }
}
