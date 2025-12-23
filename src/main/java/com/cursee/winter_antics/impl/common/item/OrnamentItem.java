package com.cursee.winter_antics.impl.common.item;

import com.cursee.winter_antics.impl.common.entity.projectile.ThrownOrnament;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class OrnamentItem extends StandingAndWallBlockItem implements ProjectileItem {

  public OrnamentItem(Block block, Block wallBlock, Direction attachmentDirection, Properties properties) {
    super(block, wallBlock, attachmentDirection, properties);
  }

  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);

    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

    if (level instanceof ServerLevel serverlevel) {
      Projectile.spawnProjectileFromRotation(ThrownOrnament::new, serverlevel, itemstack, player, 0.0F, 1.5F, 1.0F);
    }

    player.awardStat(Stats.ITEM_USED.get(this));
    itemstack.consume(1, player);
    return InteractionResult.SUCCESS;
  }

  @Override
  public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction dir) {
    return new ThrownOrnament(level, pos.x(), pos.y(), pos.z(), stack);
  }
}
