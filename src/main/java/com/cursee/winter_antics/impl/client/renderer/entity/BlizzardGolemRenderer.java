package com.cursee.winter_antics.impl.client.renderer.entity;

import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.client.model.BlizzardGolemModel;
import com.cursee.winter_antics.impl.client.renderer.entity.layers.BlizzardGolemHeadLayer;
import com.cursee.winter_antics.impl.common.entity.BlizzardGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
import net.minecraft.resources.Identifier;

public class BlizzardGolemRenderer extends MobRenderer<BlizzardGolem, SnowGolemRenderState, BlizzardGolemModel> {

  private static final Identifier SNOW_GOLEM_LOCATION = WinterAntics.identifier("textures/entity/blizzard_golem.png");

  public BlizzardGolemRenderer(EntityRendererProvider.Context context) {
    super(context, new BlizzardGolemModel(context.bakeLayer(BlizzardGolemModel.LAYER_LOCATION)), 0.5F);
    this.addLayer(new BlizzardGolemHeadLayer(this, context.getBlockRenderDispatcher()));
  }

  public Identifier getTextureLocation(SnowGolemRenderState p_469701_) {
    return SNOW_GOLEM_LOCATION;
  }

  public SnowGolemRenderState createRenderState() {
    return new SnowGolemRenderState();
  }

  public void extractRenderState(BlizzardGolem golem, SnowGolemRenderState state, float partialTick) {
    super.extractRenderState(golem, state, partialTick);
    state.hasPumpkin = golem.hasPumpkin();
  }
}
