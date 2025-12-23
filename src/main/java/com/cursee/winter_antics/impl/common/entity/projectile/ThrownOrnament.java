package com.cursee.winter_antics.impl.common.entity.projectile;

import com.cursee.winter_antics.impl.common.registry.WAItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownOrnament extends ThrowableItemProjectile {

  private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

  public ThrownOrnament(EntityType<? extends ThrownEgg> p_478861_, Level p_478341_) {
    super(p_478861_, p_478341_);
  }

  public ThrownOrnament(Level level, LivingEntity owner, ItemStack item) {
    super(EntityType.EGG, owner, level, item);
  }

  public ThrownOrnament(Level level, double x, double y, double z, ItemStack item) {
    super(EntityType.EGG, x, y, z, level, item);
  }

  @Override
  public void handleEntityEvent(byte entityEventId) {
    // if (entityEventId == 3) {
    if (entityEventId == EntityEvent.DEATH) {
      double d0 = 0.08;

      for (int i = 0; i < 8; ++i) {
        this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - (double) 0.5F) * 0.08,
            ((double) this.random.nextFloat() - (double) 0.5F) * 0.08, ((double) this.random.nextFloat() - (double) 0.5F) * 0.08);
      }
    }

  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    super.onHitEntity(result);
    result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);
    if (!(this.level() instanceof ServerLevel serverLevel)) {
      return;
    }
    serverLevel.playSound(null, this.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);
    this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
    this.discard();
  }

  @Override
  protected Item getDefaultItem() {
    return WAItems.ORNAMENT;
  }

  @Override
  public ItemStack getItem() {
    return new ItemStack(getDefaultItem());
  }
}
