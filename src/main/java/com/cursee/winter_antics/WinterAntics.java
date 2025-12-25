package com.cursee.winter_antics;

import com.cursee.winter_antics.impl.common.config.WAConfig;
import com.cursee.winter_antics.impl.common.entity.BlizzardGolem;
import com.cursee.winter_antics.impl.common.entity.SnowAngel;
import com.cursee.winter_antics.impl.common.registry.WABlockEntities;
import com.cursee.winter_antics.impl.common.registry.WABlocks;
import com.cursee.winter_antics.impl.common.registry.WAEntities;
import com.cursee.winter_antics.impl.common.registry.WAItems;
import com.cursee.winter_antics.impl.server.config.WAServerConfig;
import com.mojang.logging.LogUtils;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Constants.MOD_ID)
public class WinterAntics {

  public static final Logger LOG = LogUtils.getLogger();
  public static IEventBus EVENT_BUS;

  // The constructor for the mod class is the first code that is run when your mod is loaded.
  // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
  public WinterAntics(IEventBus modEventBus, ModContainer modContainer) {

    EVENT_BUS = modEventBus;

    bind(Registries.ENTITY_TYPE, WAEntities::register);
    bind(Registries.BLOCK, WABlocks::register);
    bind(Registries.BLOCK_ENTITY_TYPE, WABlockEntities::register);
    bind(Registries.ITEM, WAItems::registerItems);
    bind(Registries.CREATIVE_MODE_TAB, WAItems::registerTabs);

    WinterAntics.LOG.info("{} common initialization is occurring.", Constants.MOD_ID);

    // Register the commonSetup method for mod loading
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::onCreateEntityAttributes);

    // means we can only subscribe to NF events, not mod events
    NeoForge.EVENT_BUS.register(this);

//    // Register the item to a creative tab
//    modEventBus.addListener(this::addCreative);

    // Register our mod's ModConfigSpec so that FML can create and load the config file for us
    modContainer.registerConfig(ModConfig.Type.COMMON, WAConfig.SPEC);
    modContainer.registerConfig(Type.SERVER, WAServerConfig.SPEC);
  }

  public static Identifier identifier(String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public static <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {
    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

//  // Add the example block item to the building blocks tab
//  private void addCreative(BuildCreativeModeTabContentsEvent event) {
//    if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//      event.accept(EXAMPLE_BLOCK_ITEM);
//    }
//  }

  private void commonSetup(FMLCommonSetupEvent event) {

    LOG.info("{} common setup is occurring.", Constants.MOD_ID);

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      LOG.info("Additional debugging logs will be written by {}", Constants.MOD_ID);
    }

//    LOG.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
//
//    Config.ITEM_STRINGS.get().forEach((item) -> LOG.info("ITEM >> {}", item));
  }

  public void onCreateEntityAttributes(EntityAttributeCreationEvent event) {

    event.put(WAEntities.SNOW_ANGEL, Mob.createMobAttributes().build());
    event.put(WAEntities.BLIZZARD_GOLEM, BlizzardGolem.createAttributes().build());
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
//    LOG.info("HELLO from server starting");
  }

  @SubscribeEvent
  public void onPlayerTick(PlayerTickEvent.Pre event) {

    if (!WAServerConfig.SPAWN_SNOW_ANGELS.getAsBoolean()) {
      return;
    }

    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    if (!(player.getRandom().nextFloat() <= 0.01) || !player.isVisuallyCrawling()) {
      return;
    }

    ServerLevel level = player.level();
    Vec3 rawPos = player.position();
    BlockPos pos = player.blockPosition();
    BlockState state = level.getBlockState(pos);

    if (state.is(Blocks.SNOW)) {

      var nearby = level.getEntities(player, player.getBoundingBox().inflate(2, 0, 2), entity -> entity instanceof SnowAngel);
      if (!nearby.isEmpty()) {
        return;
      }

      SnowAngel snowAngel = WAEntities.SNOW_ANGEL.create(level, EntitySpawnReason.NATURAL);
      if (snowAngel == null) {
        return;
      }

      snowAngel.forceSetRotation(player.yRotO, true, player.xRotO, true);

      // snowAngel.snapTo(pos.getX(), pos.getY(), pos.getZ());
      snowAngel.snapTo(rawPos);

      level.addFreshEntity(snowAngel);
      snowAngel.gameEvent(GameEvent.ENTITY_PLACE, player);

      if (WAConfig.DEBUGGING.getAsBoolean()) {
        LOG.info("Spawned new Snow Angel in dimension {} at position {}", level.dimension(), pos.toShortString());
      }
    }
  }
}
