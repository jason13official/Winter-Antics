package com.cursee.winter_antics.impl.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class BlizzardGolem extends AbstractGolem implements Shearable, RangedAttackMob {

  private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(BlizzardGolem.class, EntityDataSerializers.BYTE);
  private static final byte PUMPKIN_FLAG = 16;
  private static final boolean DEFAULT_PUMPKIN = true;

  public BlizzardGolem(EntityType<? extends BlizzardGolem> entityType, Level level) {
    super(entityType, level);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0F).add(Attributes.MOVEMENT_SPEED, 0.25F);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.5F, 5, 15.0F));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F, 1.0000001E-5F));
    // this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    // this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (mob, serverLevel) -> mob instanceof Enemy));
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false, (living, serverLevel) -> canAttack(living)));
  }

//  private boolean canWeAttack(LivingEntity living) {
//
//    boolean stillAlive = !living.isDeadOrDying();
//
//    boolean probablySurvival
//
//    return stillAlive;
//  }

  @Override
  public boolean canAttack(LivingEntity living) {

    // ignores invulnerable/invisible players, as well as players in peaceful
    boolean canSeeAsEnemy = living.canBeSeenAsEnemy();
    boolean difficultyValid = living.level().getDifficulty() != Difficulty.PEACEFUL;
    boolean stillAlive = !living.isDeadOrDying();
    boolean differentSpecies = !(living instanceof BlizzardGolem);

    return differentSpecies && stillAlive && difficultyValid && canSeeAsEnemy;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(DATA_PUMPKIN_ID, (byte) PUMPKIN_FLAG);
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);
    output.putBoolean("Pumpkin", this.hasPumpkin());
  }

  @Override
  protected void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);
    this.setPumpkin(input.getBooleanOr("Pumpkin", DEFAULT_PUMPKIN));
  }

  @Override
  public boolean isSensitiveToWater() {
    return true;
  }

  @Override
  public void aiStep() {
    super.aiStep();
    Level level = this.level();
    if (level instanceof ServerLevel serverLevel) {
      if (serverLevel.environmentAttributes().getValue(EnvironmentAttributes.SNOW_GOLEM_MELTS, this.position())) {
        this.hurtServer(serverLevel, this.damageSources().onFire(), 0.5F);
      }

      if (!EventHooks.canEntityGrief(serverLevel, this)) {
        return;
      }

      BlockState state = Blocks.SNOW.defaultBlockState();

      for (int mod = 0; mod < 4; ++mod) {
        int x = Mth.floor(this.getX() + (double) ((float) (mod % 2 * 2 - 1) * 0.25F));
        int y = Mth.floor(this.getY());
        int z = Mth.floor(this.getZ() + (double) ((float) (mod / 2 % 2 * 2 - 1) * 0.25F));
        BlockPos pos = new BlockPos(x, y, z);
        if (this.level().getBlockState(pos).isAir() && state.canSurvive(this.level(), pos)) {
          this.level().setBlockAndUpdate(pos, state);
          this.level().gameEvent(GameEvent.BLOCK_PLACE, pos, Context.of(this, state));
        }
      }
    }

  }

  @Override
  public void performRangedAttack(LivingEntity target, float distanceFactor) {
    double deltaX = target.getX() - this.getX();
    double y = target.getEyeY() - (double) 1.1F;
    double deltaZ = target.getZ() - this.getZ();
    double distSqrt = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * (double) 0.2F;
    Level level = this.level();
    if (level instanceof ServerLevel serverLevel) {
      ItemStack stack = new ItemStack(Items.SNOWBALL);
      ItemStack stack1 = new ItemStack(Items.ENDER_PEARL);
      Projectile.spawnProjectile(new Snowball(serverLevel, this, stack), serverLevel, stack, (proj) -> proj.shoot(deltaX, y + distSqrt - proj.getY(), deltaZ, 1.6F, 12.0F));
      Projectile.spawnProjectile(new SmallFireball(serverLevel, this, Vec3.ZERO), serverLevel, stack, (proj) -> proj.shoot(deltaX, y + distSqrt - proj.getY(), deltaZ, 1.6F, 12.0F));
      Projectile.spawnProjectile(new ThrownEnderpearl(serverLevel, this, stack1), serverLevel, stack, (proj) -> proj.shoot(deltaX, y + distSqrt - proj.getY(), deltaZ, 1.6F, 12.0F));
    }

    this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
  }

  @Override
  protected InteractionResult mobInteract(Player player, InteractionHand hand) {
    player.getItemInHand(hand);
    return InteractionResult.PASS;
  }

  @Override
  public void shear(ServerLevel serverLevel, SoundSource soundSource, ItemStack stack) {
    serverLevel.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
    this.setPumpkin(false);
    this.dropFromShearingLootTable(serverLevel, BuiltInLootTables.SHEAR_SNOW_GOLEM, stack, (level, itemStack) -> this.spawnAtLocation(level, itemStack, this.getEyeHeight()));
  }

  @Override
  public boolean readyForShearing() {
    return this.isAlive() && this.hasPumpkin();
  }

  public boolean hasPumpkin() {
    return (this.entityData.get(DATA_PUMPKIN_ID) & PUMPKIN_FLAG) != 0;
  }

  public void setPumpkin(boolean pumpkinEquipped) {
    byte flag = this.entityData.get(DATA_PUMPKIN_ID);
    if (pumpkinEquipped) {
      this.entityData.set(DATA_PUMPKIN_ID, (byte) (flag | PUMPKIN_FLAG));
    } else {
      this.entityData.set(DATA_PUMPKIN_ID, (byte) (flag & -17));
    }

  }

  @Override
  protected @Nullable SoundEvent getAmbientSound() {
    return SoundEvents.SNOW_GOLEM_AMBIENT;
  }

  @Override
  protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.SNOW_GOLEM_HURT;
  }

  @Override
  protected @Nullable SoundEvent getDeathSound() {
    return SoundEvents.SNOW_GOLEM_DEATH;
  }

  @Override
  public Vec3 getLeashOffset() {
    return new Vec3(0.0F, 0.75F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
  }
}
