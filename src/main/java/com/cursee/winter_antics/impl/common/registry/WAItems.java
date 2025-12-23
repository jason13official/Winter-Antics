package com.cursee.winter_antics.impl.common.registry;

import com.cursee.winter_antics.Constants;
import com.cursee.winter_antics.WinterAntics;
import com.cursee.winter_antics.impl.common.item.OrnamentItem;
import java.util.function.BiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WAItems {

  public static Identifier BLIZZARD_ID;
  public static ResourceKey<Item> BLIZZARD_KEY;
  public static Item BLIZZARD;

  public static Identifier ORNAMENT_ID;
  public static ResourceKey<Item> ORNAMENT_KEY;
  public static Item ORNAMENT;

  public static Identifier WINTER_ANTICS_TAB_ID;
  public static CreativeModeTab WINTER_ANTICS_TAB;

  public static void registerItems(BiConsumer<Item, Identifier> consumer) {

    BLIZZARD_ID = WinterAntics.identifier("blizzard");
    BLIZZARD_KEY = ResourceKey.create(Registries.ITEM, BLIZZARD_ID);
    BLIZZARD = new BlockItem(WABlocks.BLIZZARD, new Properties().setId(BLIZZARD_KEY));

    ORNAMENT_ID = WinterAntics.identifier("ornament");
    ORNAMENT_KEY = ResourceKey.create(Registries.ITEM, ORNAMENT_ID);
    ORNAMENT = new OrnamentItem(WABlocks.ORNAMENT_FLOOR, WABlocks.ORNAMENT_WALL, Direction.DOWN, new Properties().stacksTo(16).setId(ORNAMENT_KEY));

    consumer.accept(BLIZZARD, BLIZZARD_ID);
    consumer.accept(ORNAMENT, ORNAMENT_ID);
  }

  public static void registerTabs(BiConsumer<CreativeModeTab, Identifier> consumer) {

    WINTER_ANTICS_TAB_ID = WinterAntics.identifier(Constants.MOD_ID);

    WINTER_ANTICS_TAB = CreativeModeTab.builder().icon(() -> new ItemStack(Items.COCOA_BEANS)).title(Component.translatable("itemGroup.winterAntics")).displayItems((itemDisplayParameters, output) -> {
      output.accept(WAItems.ORNAMENT);
      output.accept(WAItems.ORNAMENT);
    }).build();

    consumer.accept(WINTER_ANTICS_TAB, WINTER_ANTICS_TAB_ID);
  }
}
