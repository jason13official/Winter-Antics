package com.cursee.winter_antics.impl.client.model;// Made with Blockbench 5.0.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cursee.winter_antics.Constants;
import com.cursee.winter_antics.impl.client.renderer.entity.state.SnowAngelRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;

public class SnowAngelModel extends EntityModel<SnowAngelRenderState> {

  // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow_angel"), "main");

  private final ModelPart main;

  public SnowAngelModel(ModelPart root) {
    super(root);
    this.main = root.getChild("main");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, -1.0F, -20.0F, 40.0F, 1.0F, 40.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 24.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 128, 128);
  }

  @Override
  public void setupAnim(SnowAngelRenderState renderState) {
    super.setupAnim(renderState);
  }

//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
}