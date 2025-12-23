package com.cursee.winter_antics.mixin;

import com.cursee.winter_antics.impl.common.registry.WABlocks;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.WeatherEffectRenderer.ColumnInstance;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

//  @Shadow
//  @Final
//  private static Identifier SNOW_LOCATION;
//
//  @Shadow
//  protected abstract void renderInstances(VertexConsumer buffer, List<ColumnInstance> columnInstances, Vec3 cameraPosition, float amount, int radius, float rainLevel);
//
//  @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/WeatherRenderState;Lnet/minecraft/client/renderer/state/LevelRenderState;)V")
//  private void winter_antics$render(MultiBufferSource bufferSource, Vec3 cameraPosition, WeatherRenderState renderState, LevelRenderState levelRenderState, CallbackInfo ci) {
//
//
//    BlockPos posContainingCamera = BlockPos.containing(cameraPosition);
//
//    if (WinterAnticsClient.BLIZZARD_POSITIONS.contains(new Pair<>(posContainingCamera.getX(), posContainingCamera.getZ()))) {
//      System.out.println("rendering?");
//      RenderType renderType = RenderTypes.weather(SNOW_LOCATION, Minecraft.useShaderTransparency());
//      this.renderInstances(bufferSource.getBuffer(renderType), renderState.snowColumns, cameraPosition, 0.8F, renderState.radius, renderState.intensity);
//    }
//  }

  @Shadow
  protected abstract Precipitation getPrecipitationAt(Level level, BlockPos pos);

  @Shadow
  protected abstract ColumnInstance createRainColumnInstance(RandomSource random, int ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTick);

  @Shadow
  protected abstract ColumnInstance createSnowColumnInstance(RandomSource random, int ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTick);

  @Inject(at = @At("TAIL"), method = "extractRenderState")
  public void extractRenderState(final Level level, final int ticks, final float partialTicks, final Vec3 cameraPos, final WeatherRenderState renderState, CallbackInfo ci) {
    renderState.intensity = level.getRainLevel(partialTicks);
    // if (!(renderState.intensity <= 0.0F)) {
    renderState.radius = Minecraft.getInstance().options.weatherRadius().get();
    int cameraBlockX = Mth.floor(cameraPos.x);
    int cameraBlockY = Mth.floor(cameraPos.y);
    int cameraBlockZ = Mth.floor(cameraPos.z);
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
    RandomSource random = RandomSource.create();

    AtomicBoolean foundBlizzardBlock = new AtomicBoolean();

    for (int z = cameraBlockZ - renderState.radius; z <= cameraBlockZ + renderState.radius; z++) {
      for (int x = cameraBlockX - renderState.radius; x <= cameraBlockX + renderState.radius; x++) {
        int terrainHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        int y0 = Math.max(cameraBlockY - renderState.radius, terrainHeight);
        int y1 = Math.max(cameraBlockY + renderState.radius, terrainHeight);

        if (y1 - y0 != 0) {
          Precipitation precipitation = this.getPrecipitationAt(level, mutablePos.set(x, cameraBlockY, z));

          BlockPos checked = mutablePos.below(1);
          if (level.getBlockState(checked).is(WABlocks.BLIZZARD)) {
            System.out.println("found a blizzard block");
            foundBlizzardBlock.set(true);
          } else if (!level.getBlockState(checked).is(BlockTags.AIR))  {
            System.out.println("found " + String.valueOf(level.getBlockState(checked).getBlock()));
          }

          if (foundBlizzardBlock.get()) {

            System.out.println("set current precipitation to true");
            precipitation = Precipitation.SNOW;
          }

          // int seed = x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761;
          int seed = 1;

          random.setSeed(seed);
          int lightSampleY = Math.max(cameraBlockY, terrainHeight);
          int lightCoords = LevelRenderer.getLightColor(level, mutablePos.set(x, lightSampleY, z));

          renderState.snowColumns.add(this.createSnowColumnInstance(random, ticks, x, y0, y1, z, lightCoords, partialTicks));
        }
      }
    }
  }
}
