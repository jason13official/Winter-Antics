package com.cursee.winter_antics.impl.client.renderer.entity.layers;

import com.cursee.winter_antics.impl.client.model.BlizzardGolemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlizzardGolemHeadLayer extends RenderLayer<SnowGolemRenderState, BlizzardGolemModel> {

  private final BlockRenderDispatcher blockRenderer;

  public BlizzardGolemHeadLayer(RenderLayerParent<SnowGolemRenderState, BlizzardGolemModel> renderer, BlockRenderDispatcher blockRenderer) {
    super(renderer);
    this.blockRenderer = blockRenderer;
  }

  public void submit(PoseStack poseStack, SubmitNodeCollector collector, int p_433104_, SnowGolemRenderState state, float p_433223_, float p_433380_) {
    if (state.hasPumpkin && (!state.isInvisible || state.appearsGlowing())) {
      poseStack.pushPose();
      this.getParentModel().getHead().translateAndRotate(poseStack);
      float f = 0.625F;
      poseStack.translate(0.0F, -0.34375F, 0.0F);
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
      poseStack.scale(0.625F, -0.625F, -0.625F);
      BlockState blockstate = Blocks.CARVED_PUMPKIN.defaultBlockState();
      BlockStateModel blockstatemodel = this.blockRenderer.getBlockModel(blockstate);
      int i = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
      poseStack.translate(-0.5F, -0.5F, -0.5F);
      RenderType rendertype = state.appearsGlowing() && state.isInvisible ? RenderTypes.outline(TextureAtlas.LOCATION_BLOCKS) : ItemBlockRenderTypes.getRenderType(blockstate);
      collector.submitBlockModel(poseStack, rendertype, blockstatemodel, 0.0F, 0.0F, 0.0F, p_433104_, i, state.outlineColor);
      poseStack.popPose();
    }

  }
}
