package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;
import static io.github.mikip98.humilityafm.registries.ItemGroupRegistry.putIntoItemGroup;

public class ItemRegistry {
    public static final Item[] GLOWING_POWDER_VARIANTS = Arrays.stream(GenerationData.vanillaColorPallet).map(color -> register("glowing_powder_" + color)).toArray(Item[]::new);

    public static Item[] CABINET_ITEM_VARIANTS;
    public static Item[] ILLUMINATED_CABINET_ITEM_VARIANTS;

    public static Item[] CANDLESTICK_ITEM_VARIANTS = new Item[GenerationData.vanillaCandlestickMetals.length];
    public static List<Item[]> RUSTABLE_CANDLESTICK_ITEM_VARIANTS = new ArrayList<>();


    public static final Item CABINET_ITEM = register(
            new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_CABINET_BLOCK, BlockRegistry.CABINET_BLOCK, new FabricItemSettings()),
            "cabinet_block"
    );
    public static final Item ILLUMINATED_CABINET_ITEM = register(
            new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, BlockRegistry.ILLUMINATED_CABINET_BLOCK, new FabricItemSettings()),
            "illuminated_cabinet_block"
    );


    public static void register() {
        putIntoItemGroup(GLOWING_POWDER_VARIANTS, ItemGroups.INGREDIENTS);
        putIntoItemGroup(CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
        putIntoItemGroup(ILLUMINATED_CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
        putIntoItemGroup(CANDLESTICK_ITEM_VARIANTS, ItemGroups.FUNCTIONAL);
        RUSTABLE_CANDLESTICK_ITEM_VARIANTS.forEach(s -> putIntoItemGroup(s, ItemGroups.FUNCTIONAL));
    }


    protected static void registerArray(Item[] items, String[] names, String prefix) {
        for (int i = 0; i < items.length; i++) {
            register(items[i], prefix + names[i]);
        }
    }
    public static Item register(Item item, String name) {
        return register(name, item);
    }
    public static Item register(String name) {
        return register(name, new Item(new FabricItemSettings()));
    }
    public static Item register(String name, Item item) {
        Registry.register(Registries.ITEM, getId(name), item);
        return item;
    }
}
