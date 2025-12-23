package com.cursee.winter_antics.impl.client.renderer.entity.state;

import com.cursee.winter_antics.WinterAntics;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SnowAngelRenderState extends LivingEntityRenderState {

  private static final Identifier DEFAULT_TEXTURE = WinterAntics.identifier("textures/entity/snow_angel/snow_angel_translucent.png");

  public Identifier texture;

  public SnowAngelRenderState() {

    this.texture = DEFAULT_TEXTURE;
  }
}
