package com.cursee.winter_antics.mixin;

import com.cursee.winter_antics.impl.common.entity.BlizzardGolem;
import com.cursee.winter_antics.impl.common.registry.WABlocks;
import com.cursee.winter_antics.impl.common.registry.WAEntities;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.function.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.FireworkExplosion.Shape;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {

  @Unique
  private static final Predicate<BlockState> WINTER_ANTICS$PUMPKINS_PREDICATE = (state) -> state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN);

  @Unique
  private @Nullable BlockPattern winter_antics$blizzardGolemBase;

  @Unique
  private @Nullable BlockPattern winter_antics$blizzardGolemFull;

  @Unique
  private static void winter_antics$spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity golem, BlockPos pos) {
    winter_antics$clearPatternBlocks(level, patternMatch);
    golem.snapTo((double) pos.getX() + (double) 0.5F, (double) pos.getY() + 0.05, (double) pos.getZ() + (double) 0.5F, 0.0F, 0.0F);
    level.addFreshEntity(golem);

    for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0F))) {
      CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
    }

    winter_antics$updatePatternBlocks(level, patternMatch);
  }

  @Unique
  private static void winter_antics$clearPatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
    for (int i = 0; i < patternMatch.getWidth(); ++i) {
      for (int j = 0; j < patternMatch.getHeight(); ++j) {
        BlockInWorld blockinworld = patternMatch.getBlock(i, j, 0);
        level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockinworld.getPos(), Block.getId(blockinworld.getState()));
      }
    }
  }

  @Unique
  private static void winter_antics$updatePatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
    for (int i = 0; i < patternMatch.getWidth(); ++i) {
      for (int j = 0; j < patternMatch.getHeight(); ++j) {
        BlockInWorld blockinworld = patternMatch.getBlock(i, j, 0);
        level.updateNeighborsAt(blockinworld.getPos(), Blocks.AIR);
      }
    }
  }

  @Inject(at = @At("HEAD"), method = "canSpawnGolem", cancellable = true)
  private void winter_antics$canSpawnGolem(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
    if (this.winter_antics$getOrCreateBlizzardGolemBase().find(level, pos) != null) {
      cir.setReturnValue(true);
    }
  }

  @Unique
  private BlockPattern winter_antics$getOrCreateBlizzardGolemBase() {

    if (this.winter_antics$blizzardGolemBase == null) {
      this.winter_antics$blizzardGolemBase = BlockPatternBuilder.start().aisle(" ", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(WABlocks.BLIZZARD))).build();
    }

    return this.winter_antics$blizzardGolemBase;
  }

  @Inject(at = @At("HEAD"), method = "trySpawnGolem", cancellable = true)
  private void winter_antics$trySpawnGolem(Level level, BlockPos pos, CallbackInfo ci) {
    BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = this.winter_antics$getOrCreateBlizzardGolemFull().find(level, pos);
    if (blockpattern$blockpatternmatch != null) {
      // SnowGolem snowgolem = (SnowGolem) EntityType.SNOW_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
      BlizzardGolem blizzardGolem = WAEntities.BLIZZARD_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
      if (blizzardGolem != null) {
        winter_antics$spawnGolemInWorld(level, blockpattern$blockpatternmatch, blizzardGolem, blockpattern$blockpatternmatch.getBlock(0, 2, 0).getPos());

        if (level instanceof ServerLevel serverLevel) {
          serverLevel.playSound(null, pos, SoundEvents.CREAKING_HEART_SPAWN, SoundSource.HOSTILE);
        }

        FireworkExplosion expl = new FireworkExplosion(Shape.BURST, IntList.of(0xFFFF0000), IntList.of(0xFF0000FF), true, true);

        var stack = new ItemStack(Items.FIREWORK_ROCKET);
        stack.set(DataComponents.FIREWORKS, new Fireworks(0, NonNullList.withSize(1, expl)));

        level.addFreshEntity(new FireworkRocketEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));

        ci.cancel();
      }
    }
  }

  @Unique
  private BlockPattern winter_antics$getOrCreateBlizzardGolemFull() {
    if (this.winter_antics$blizzardGolemFull == null) {
      this.winter_antics$blizzardGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(WINTER_ANTICS$PUMPKINS_PREDICATE))
          .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(WABlocks.BLIZZARD))).build();
    }

    return this.winter_antics$blizzardGolemFull;
  }
}
