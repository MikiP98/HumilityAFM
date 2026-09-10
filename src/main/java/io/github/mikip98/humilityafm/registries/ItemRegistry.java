package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.content.items.ModVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.material_management.SizedIterable;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import net.minecraft.core.Direction;
#if MC_VERSION < 12104
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
#else
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
#endif
import net.minecraft.world.item.Item;
#if POLYMER import net.minecraft.world.level.block.Block; #endif
#if MC_VERSION >= 12104 import net.minecraft.world.item.Items; #endif

import java.util.Arrays;
#if POLYMER import java.util.IdentityHashMap; #endif
#if POLYMER import java.util.Map; #endif
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ItemRegistry {
    public static final Item[] GLOWING_POWDER_VARIANTS = ModConfig.getEnableColouredFeatureSetBeta()
                    ? Arrays.stream(RawGenerationData.vanillaColorPallet).map(color -> register("glowing_powder_" + color)).toArray(Item[]::new)
                    : null;

    public static final Item CABINET_ITEM = register(
            "cabinet_block",
            (settings) -> new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_CABINET_BLOCK, BlockRegistry.CABINET_BLOCK, settings)
    );
    public static final Item ILLUMINATED_CABINET_ITEM = register(
            "illuminated_cabinet_block",
            (settings) -> new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, BlockRegistry.ILLUMINATED_CABINET_BLOCK, settings)
    );
    public static Item[] CABINET_ITEM_VARIANTS;
    public static Item[] ILLUMINATED_CABINET_ITEM_VARIANTS;

    public static Item[] CANDLESTICK_ITEM_VARIANTS;
    public static Item[][] RUSTABLE_CANDLESTICK_ITEM_VARIANTS;

    public static Item[] COLOURED_TORCH_ITEM_VARIANTS;

    #if POLYMER
    public static final Map<Block, Item> OPEN_CABINET_ITEMS = new IdentityHashMap<>();
    #endif


    public static void register() {
        int cabinetAmount = BlockRegistry.WALL_CABINET_BLOCK_VARIANTS.length;
        CABINET_ITEM_VARIANTS = new Item[cabinetAmount];
        ILLUMINATED_CABINET_ITEM_VARIANTS = new Item[cabinetAmount];
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            final int finalI = i;
            CABINET_ITEM_VARIANTS[i] = register(
                    "cabinet_" + material.getSafeName(),
                    (settings) -> new DoubleVerticallyAttachableBlockItem(
                            BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[finalI],
                            BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[finalI],
                            settings
                    )
            );
            ILLUMINATED_CABINET_ITEM_VARIANTS[i] = register(
                    "illuminated_cabinet_" + material.getSafeName(),
                    (settings) -> new DoubleVerticallyAttachableBlockItem(
                            BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[finalI],
                            BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[finalI],
                            settings
                    )
            );

            #if POLYMER
            Item openWall = register("open_wall_cabinet_" + material.getSafeName());
            Item openFloor = register("open_floor_cabinet_" + material.getSafeName());
            Item openWallIllum = register("open_wall_illuminated_cabinet_" + material.getSafeName());
            Item openFloorIllum = register("open_floor_illuminated_cabinet_" + material.getSafeName());

            OPEN_CABINET_ITEMS.put(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i], openWall);
            OPEN_CABINET_ITEMS.put(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i], openFloor);
            OPEN_CABINET_ITEMS.put(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], openWallIllum);
            OPEN_CABINET_ITEMS.put(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], openFloorIllum);
            #endif

            ++i;
        }

        // CANDLESTICK BETA
        if (ModConfig.getEnableCandlestickBeta()) {
            CANDLESTICK_ITEM_VARIANTS = new Item[ActiveGenerationData.simpleCandlestickMaterials.size()];
            i = 0;
            for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
                final int finalI = i;
                CANDLESTICK_ITEM_VARIANTS[i] = register(
                        "candlestick_" + material.getSafeName(),
                        (settings) -> new ModVerticallyAttachableBlockItem(
                                BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS[finalI],
                                BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS[finalI],
                                settings,
                                Direction.DOWN
                        )
                );
                ++i;
            }
            RUSTABLE_CANDLESTICK_ITEM_VARIANTS = new Item[ActiveGenerationData.rustingCandlestickMaterials.length][];
            i = 0;
            for (SizedIterable<BlockMaterial> materialSet : ActiveGenerationData.rustingCandlestickMaterials) {
                final int finalI = i;
                RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i] = new Item[materialSet.size()];
                int j = 0;
                for (BlockMaterial material : materialSet) {
                    final int finalJ = j;
                    RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][j] = register(
                            "candlestick_" + material.getSafeName(),
                            (settings) -> new ModVerticallyAttachableBlockItem(
                                    BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[finalI][finalJ],
                                    BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[finalI][finalJ],
                                    settings,
                                    Direction.DOWN
                            )
                    );
                    ++j;
                }
                ++i;
            }
        }
        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            COLOURED_TORCH_ITEM_VARIANTS = new Item[BlockRegistry.COLOURED_TORCH_VARIANTS.length];
            i = 0;
            for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
                final int finalI = i;
                COLOURED_TORCH_ITEM_VARIANTS[i] = register(
                        "coloured_torch_" + material.getRawName(),
                        (settings) -> new ModVerticallyAttachableBlockItem(
                                BlockRegistry.COLOURED_TORCH_VARIANTS[finalI],
                                BlockRegistry.COLOURED_WALL_TORCH_VARIANTS[finalI],
                                settings,
                                Direction.DOWN
                        )
                );
                ++i;
            }
        }
    }


    public static Item register(String name) {
        return register(name, #if POLYMER Polymer.PolymerItemImpl::new #else Item::new #endif);
    }
    public static Item register(String name, Function<Item.Properties, Item> factory) {
        return register(name, factory, new Item.Properties());
    }
    #if MC_VERSION < 12104
    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        final Item item = Registry.register(BuiltInRegistries.ITEM, getId(name), factory.apply(settings));
        #if POLYMER Polymer.requestPolymerModel(item, name); #endif
        return item;
    }
    #else
    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, getId(name));
        final Item item = #if MC_VERSION < 260000 Items. #endif registerItem(registryKey, factory, settings);
        #if POLYMER Polymer.requestPolymerModel(item, name); #endif
        return item;
    }
    #endif
    #if MC_VERSION > 260000
    protected static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = (Item) itemFactory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return (Item) Registry.register(BuiltInRegistries.ITEM, key, item);
    }
    #endif
}
