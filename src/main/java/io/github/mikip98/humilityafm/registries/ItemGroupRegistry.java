package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.generators.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.Arrays;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ItemGroupRegistry {
    // Cabinet item group
    final static ItemGroup CABINETS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemRegistry.CABINET_ITEM))
            .displayName(Text.translatable("itemGroup.cabinets"))
            .entries((displayContext, entries) -> {
                Arrays.stream(ItemRegistry.CABINET_ITEM_VARIANTS).forEach(entries::add);
                Arrays.stream(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS).forEach(entries::add);
            })
            .build();

    // InnerOuterStairs item group
    final static ItemGroup INNER_OUTER_STAIRS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BlockRegistry.OUTER_STAIRS))
            .displayName(Text.translatable("itemGroup.innerOuterStairs"))
            .entries((displayContext, entries) -> {
                Arrays.stream(ForcedCornerStairsGenerator.innerStairsBlockVariants).forEach(entries::add);
                Arrays.stream(ForcedCornerStairsGenerator.outerStairsBlockVariants).forEach(entries::add);
            })
            .build();

    // WoodenMosaic item group
    final static ItemGroup WOODEN_MOSAIC_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(WoodenMosaicGenerator.woodenMosaicVariants[0]))
            .displayName(Text.translatable("itemGroup.woodenMosaics"))
            .entries((displayContext, entries) -> Arrays.stream(WoodenMosaicGenerator.woodenMosaicVariants).forEach(entries::add))
            .build();

    // TerracottaTiles item group
    final static ItemGroup TERRACOTTA_TILES_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TerracottaTilesGenerator.terracottaTilesVariants[0]))
            .displayName(Text.translatable("itemGroup.terracottaTiles"))
            .entries((displayContext, entries) -> Arrays.stream(TerracottaTilesGenerator.terracottaTilesVariants).forEach(entries::add))
            .build();

    // Miscellaneous (Humility Misc) item group
    final static ItemGroup HUMILITY_MISCELLANEOUS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BlockRegistry.JACK_O_LANTERN_SOUL))
            .displayName(Text.translatable("itemGroup.humilityMisc"))
            .entries((displayContext, entries) -> {
                entries.add(BlockRegistry.JACK_O_LANTERN_REDSTONE);
                entries.add(BlockRegistry.JACK_O_LANTERN_SOUL);
                Arrays.stream(BlockRegistry.COLOURED_WEAK_JACK_O_LANTERNS).forEach(entries::add);
                Arrays.stream(BlockRegistry.COLOURED_JACK_O_LANTERNS).forEach(entries::add);
                Arrays.stream(BlockRegistry.COLOURED_STRONG_JACK_O_LANTERNS).forEach(entries::add);
                Arrays.stream(ItemRegistry.GLOWING_POWDER_VARIANTS).forEach(entries::add);
                Arrays.stream(ColouredFeatureSetGenerator.colouredTorchWeakVariants).forEach(entries::add);
                Arrays.stream(ColouredFeatureSetGenerator.colouredTorchVariants).forEach(entries::add);
                Arrays.stream(ColouredFeatureSetGenerator.colouredTorchStrongVariants).forEach(entries::add);
                Arrays.stream(ColouredFeatureSetGenerator.LightStripBlockVariants).forEach(entries::add);
                Arrays.stream(ItemRegistry.CANDLESTICK_ITEM_VARIANTS).forEach(entries::add);
                ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.forEach(set -> Arrays.stream(set).forEach(entries::add));
            }).build();


    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, getId("cabinets_group"), CABINETS_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("inner_outer_stairs_group"), INNER_OUTER_STAIRS_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("wooden_mosaics_group"), WOODEN_MOSAIC_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("terracotta_tiles_group"), TERRACOTTA_TILES_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("humility_misc_group"), HUMILITY_MISCELLANEOUS_GROUP);
    }


    public static void putIntoItemGroup(ItemConvertible[] items, RegistryKey<ItemGroup> group) {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> Arrays.stream(items).forEach(content::add));
    }
    public static void putIntoItemGroup(ItemConvertible item, RegistryKey<ItemGroup> group) {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item));
    }
}
