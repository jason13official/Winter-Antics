package com.cursee.winter_antics.impl.common.entity;

import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

public class SnowAngel extends Mob {

  private static final EntityDataAccessor<Integer> DATA_LIFE_ID = SynchedEntityData.defineId(SnowAngel.class, EntityDataSerializers.INT);

  private final int life = 1_200;

  public SnowAngel(EntityType<? extends Mob> type, Level level) {
    super(type, level);
    this.setNoGravity(true);
    this.blocksBuilding = false;
  }

//  public SnowAngel(Level level) {
//    this(WAEntities.SNOW_ANGEL, level);
//  }

  @Override
  protected void defineSynchedData(Builder builder) {
    super.defineSynchedData(builder);
    builder.define(DATA_LIFE_ID, 1_200);
  }

  public int getLife() {
    return this.entityData.get(DATA_LIFE_ID);
  }

  public void setLife(int value) {
    this.entityData.set(DATA_LIFE_ID, value);
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);

    output.putInt("life", this.getLife());
  }

  @Override
  protected void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);

    this.setLife(input.getIntOr("life", 1_200));
  }

  @Override
  protected boolean wouldNotSuffocateAtTargetPose(Pose pose) {
    return true; // never suffocate
  }

  @Override
  public boolean hurtServer(ServerLevel level, DamageSource source, float p_376610_) {
    return !source.is(DamageTypes.IN_WALL);
  }

  @Override
  public void baseTick() {
    super.baseTick();

    int currentLife = this.getLife();

    if (currentLife >= 1) {
      this.setLife(currentLife - 1);
    } else {
      this.discard();
    }
  }

  @Override
  public boolean isEffectiveAi() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public boolean isPushedByFluid(FluidType type) {
    return false;
  }

  public boolean shouldRenderAtSqrDistance(double distance) {
    double d0 = this.getBoundingBox().getSize() * (double) 4.0F;
    if (Double.isNaN(d0) || d0 == (double) 0.0F) {
      d0 = 4.0F;
    }

    d0 *= 64.0F;
    return distance < d0 * d0;
  }

  @Override
  public void travel(Vec3 travelVector) {
    // no op
  }

  @Override
  public boolean isPickable() {
    return false;
  }

  @Override
  public boolean canBeCollidedWith(@Nullable Entity entity) {
    return false;
  }

  @Override
  public boolean canCollideWith(Entity entity) {
    return false;
  }

  @Override
  public void push(Vec3 vector) {
    // no op
  }

  @Override
  public void push(double x, double y, double z) {
    // no op
  }

  @Override
  public void push(Entity entity) {
    // no op
  }

  @Override
  protected void pushEntities() {
    // no op
  }

  @Override
  public boolean collidedWithShapeMovingFrom(Vec3 from, Vec3 to, List<AABB> boxes) {
    return false;
  }

  public LivingEntity.Fallsounds getFallSounds() {
    return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
  }

  protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ARMOR_STAND_HIT;
  }

  protected @Nullable SoundEvent getDeathSound() {
    return SoundEvents.ARMOR_STAND_BREAK;
  }

  public void thunderHit(ServerLevel level, LightningBolt lightning) {
  }

  public boolean isAffectedByPotions() {
    return false;
  }

  @Override
  public boolean attackable() {
    return false;
  }
}
