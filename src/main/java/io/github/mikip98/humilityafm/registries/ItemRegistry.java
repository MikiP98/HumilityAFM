package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;
import static io.github.mikip98.humilityafm.registries.ItemGroupRegistry.putIntoItemGroup;

public class ItemRegistry {
    public static final Item[] GLOWING_POWDER_VARIANTS = registerArray("glowing_powder_", GenerationData.vanillaColorPallet, Item::new);

    public static Item[] CABINET_ITEM_VARIANTS;
    public static Item[] ILLUMINATED_CABINET_ITEM_VARIANTS;

    public static Item[] CANDLESTICK_ITEM_VARIANTS = new Item[GenerationData.vanillaCandlestickMetals.length];
    public static List<Item[]> RUSTABLE_CANDLESTICK_ITEM_VARIANTS = new ArrayList<>();


    public static final Item CABINET_ITEM = register(
            "cabinet_block",
            (settings) -> new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_CABINET_BLOCK, BlockRegistry.CABINET_BLOCK, settings),
            new Item.Settings()
    );
    public static final Item ILLUMINATED_CABINET_ITEM = register(
            "illuminated_cabinet_block",
            (settings) -> new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, BlockRegistry.ILLUMINATED_CABINET_BLOCK, settings),
            new Item.Settings()
    );


    public static void register() {
        putIntoItemGroup(CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
        putIntoItemGroup(ILLUMINATED_CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
        if (ModConfig.enableCandlestickBeta) {
            putIntoItemGroup(CANDLESTICK_ITEM_VARIANTS, ItemGroups.FUNCTIONAL);
            RUSTABLE_CANDLESTICK_ITEM_VARIANTS.forEach(s -> putIntoItemGroup(s, ItemGroups.FUNCTIONAL));
        }
        if (ModConfig.enableColouredFeatureSetBeta) {
            putIntoItemGroup(GLOWING_POWDER_VARIANTS, ItemGroups.INGREDIENTS);
        }
    }


    public static Item[] registerArray(String prefix, String[] names, Function<Item.Settings, Item> factory) {
        return Arrays.stream(names).map(name -> register(prefix + name, factory)).toArray(Item[]::new);
    }
    public static Item register(String name, Function<Item.Settings, Item> factory) {
        return register(name, factory, new Item.Settings());
    }
    public static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, getId(name));
        return Items.register(registryKey, factory, settings);
    }
}
