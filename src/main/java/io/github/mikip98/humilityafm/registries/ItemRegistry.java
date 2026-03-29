package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.content.items.ModVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.material_management.SizedIterable;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
#if MC_VERSION >= 260000
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
#else
import net.minecraft.item.Item;
import net.minecraft.item.Items;
#if MC_VERSION < 12104
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
#else
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
#endif
import net.minecraft.util.math.Direction;
#endif

import java.util.Arrays;
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
        return register(name, Item::new);
    }
    public static Item register(String name, Function<Item.Settings, Item> factory) {
        return register(name, factory, new Item.Settings());
    }
    #if MC_VERSION < 12104
    public static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Registry.register(Registries.ITEM, getId(name), factory.apply(settings));
    }
    #else
    public static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, getId(name));
        return Items.register(registryKey, factory, settings);
    }
    #endif
}
