package com.cursee.winter_antics.mixin;

import com.cursee.winter_antics.impl.client.WinterAnticsClient;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.WeatherEffectRenderer.ColumnInstance;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Pair;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

  @Shadow
  @Final
  private static Identifier SNOW_LOCATION;

  @Shadow
  protected abstract void renderInstances(VertexConsumer buffer, List<ColumnInstance> columnInstances, Vec3 cameraPosition, float amount, int radius, float rainLevel);

  @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/WeatherRenderState;Lnet/minecraft/client/renderer/state/LevelRenderState;)V")
  private void winter_antics$render(MultiBufferSource bufferSource, Vec3 cameraPosition, WeatherRenderState renderState, LevelRenderState levelRenderState, CallbackInfo ci) {

    BlockPos posContainingCamera = BlockPos.containing(cameraPosition);

    if (WinterAnticsClient.BLIZZARD_POSITIONS.contains(new Pair<>(posContainingCamera.getX(), posContainingCamera.getZ()))) {
      RenderType renderType = RenderTypes.weather(SNOW_LOCATION, Minecraft.useShaderTransparency());
      this.renderInstances(bufferSource.getBuffer(renderType), renderState.snowColumns, cameraPosition, 0.8F, renderState.radius, renderState.intensity);
    }
  }
}
