package com.cursee.winter_antics.impl.common.registry;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.common.entity.SnowAngel;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class WAEntities {

  public static Identifier SNOW_ANGEL_ID;
  public static ResourceKey<EntityType<?>> SNOW_ANGEL_KEY;
  public static EntityType<SnowAngel> SNOW_ANGEL;

  public static void register(BiConsumer<EntityType<?>, Identifier> consumer) {

    SNOW_ANGEL_ID = WinterAntics.identifier("snow_angel");
    SNOW_ANGEL_KEY = ResourceKey.create(Registries.ENTITY_TYPE, SNOW_ANGEL_ID);
    SNOW_ANGEL = EntityType.Builder.<SnowAngel>of(SnowAngel::new, MobCategory.MISC)
        .noLootTable()
        .fireImmune()
        .sized(6.0F, 0.5F)
        .clientTrackingRange(10)
        .updateInterval(Integer.MAX_VALUE)
        .build(SNOW_ANGEL_KEY);

    consumer.accept(SNOW_ANGEL, SNOW_ANGEL_ID);
  }
}
