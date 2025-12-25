package com.cursee.winter_antics.impl.client;

import com.cursee.winter_antics.Constants;
import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.client.model.BlizzardGolemModel;
import com.cursee.winter_antics.impl.client.model.SnowAngelModel;
import com.cursee.winter_antics.impl.client.renderer.entity.BlizzardGolemRenderer;
import com.cursee.winter_antics.impl.client.renderer.entity.SnowAngelRenderer;
import com.cursee.winter_antics.impl.common.block.OrnamentBlock;
import com.cursee.winter_antics.impl.common.config.WAConfig;
import com.cursee.winter_antics.impl.common.registry.WABlocks;
import com.cursee.winter_antics.impl.common.registry.WAEntities;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class WinterAnticsClient {

  public WinterAnticsClient(ModContainer container) {

    WinterAntics.LOG.info("{} initializing on the client.", Constants.MOD_ID);

    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

//  @SubscribeEvent
//  public static void onClientSetup(FMLClientSetupEvent event) {
//    if (WAConfig.DEBUGGING.getAsBoolean()) {
//      WinterAntics.LOG.info("{} client setup is occurring.", Constants.MOD_ID);
//    }
//  }

  @SubscribeEvent
  public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("Registering block color handlers for {}", Constants.MOD_ID);
    }

    event.register((blockState, blockAndTintGetter, blockPos, index) -> {

      if (index == 0 && blockState.getBlock() instanceof OrnamentBlock) {
        MapColor mapColor = MapColor.byId(blockState.getValue(OrnamentBlock.MAP_COLOR));
        return mapColor.col;
      }

//      return MapColor.byId(blockState.getValue(OrnamentBlock.MAP_COLOR)).col;

      return 0xFFFFFFFF;

    }, WABlocks.ORNAMENT_FLOOR, WABlocks.ORNAMENT_WALL);
  }

  @SubscribeEvent
  public static void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("Registering entity layer definitions for {}", Constants.MOD_ID);
    }

    event.registerLayerDefinition(SnowAngelModel.LAYER_LOCATION, SnowAngelModel::createBodyLayer);
    event.registerLayerDefinition(BlizzardGolemModel.LAYER_LOCATION, BlizzardGolemModel::createBodyLayer);
  }

  @SubscribeEvent
  public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("Registering entity renderers for {}", Constants.MOD_ID);
    }

    event.registerEntityRenderer(WAEntities.SNOW_ANGEL, SnowAngelRenderer::new);
    event.registerEntityRenderer(WAEntities.BLIZZARD_GOLEM, BlizzardGolemRenderer::new);
  }
}
