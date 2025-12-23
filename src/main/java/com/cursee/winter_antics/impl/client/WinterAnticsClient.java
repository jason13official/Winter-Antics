package com.cursee.winter_antics.impl.client;

import com.cursee.winter_antics.Constants;
import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.client.model.SnowAngelModel;
import com.cursee.winter_antics.impl.client.renderer.entity.SnowAngelRenderer;
import com.cursee.winter_antics.impl.common.config.WAConfig;
import com.cursee.winter_antics.impl.common.registry.WAEntities;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;
import oshi.util.tuples.Pair;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class WinterAnticsClient {

  public static final ArrayList<Pair<Integer, Integer>> BLIZZARD_POSITIONS = new ArrayList<>();

  public WinterAnticsClient(ModContainer container) {

    WinterAntics.LOG.info("{} initializing on the client.", Constants.MOD_ID);

    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    if (WAConfig.DEBUGGING.getAsBoolean()) {
      WinterAntics.LOG.info("{} client setup is occurring.", Constants.MOD_ID);
    }
  }

  @SubscribeEvent
  public static void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(SnowAngelModel.LAYER_LOCATION, SnowAngelModel::createBodyLayer);
  }

  @SubscribeEvent
  public static void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(WAEntities.SNOW_ANGEL, SnowAngelRenderer::new);
  }
}
