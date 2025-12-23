package com.cursee.winter_antics.impl.client.renderer.entity;

import com.cursee.winter_antics.impl.client.model.SnowAngelModel;
import com.cursee.winter_antics.impl.client.renderer.entity.state.SnowAngelRenderState;
import com.cursee.winter_antics.impl.common.entity.SnowAngel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SnowAngelRenderer extends MobRenderer<SnowAngel, SnowAngelRenderState, SnowAngelModel> {

  public SnowAngelRenderer(Context context) {
    super(context, new SnowAngelModel(context.bakeLayer(SnowAngelModel.LAYER_LOCATION)), 0.0f);
  }

  @Override
  public SnowAngelRenderState createRenderState() {
    return new SnowAngelRenderState();
  }

  @Override
  public Identifier getTextureLocation(SnowAngelRenderState snowAngelRenderState) {
    return snowAngelRenderState.texture;
  }
}
