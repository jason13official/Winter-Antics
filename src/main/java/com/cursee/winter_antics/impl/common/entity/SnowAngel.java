package com.cursee.winter_antics.impl.common.entity;

import com.cursee.winter_antics.impl.common.registry.WAEntities;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SnowAngel extends Mob {

  public SnowAngel(EntityType<? extends Mob> type, Level level) {
    super(type, level);
  }

  public SnowAngel(Level level) {
    super(WAEntities.SNOW_ANGEL, level);
    this.setNoGravity(true);
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
}
