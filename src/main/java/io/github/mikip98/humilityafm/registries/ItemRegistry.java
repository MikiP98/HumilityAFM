package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.content.items.ModVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.material_management.SizedIterable;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ItemRegistry {
    public static final Item[] GLOWING_POWDER_VARIANTS = ModConfig.getEnableColouredFeatureSetBeta()
                    ? Arrays.stream(RawGenerationData.vanillaColorPallet).map(color -> register("glowing_powder_" + color)).toArray(Item[]::new)
                    : null;

    public static final Item CABINET_ITEM = register(
            new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_CABINET_BLOCK, BlockRegistry.CABINET_BLOCK, new FabricItemSettings()),
            "cabinet_block"
    );
    public static final Item ILLUMINATED_CABINET_ITEM = register(
            new DoubleVerticallyAttachableBlockItem(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, BlockRegistry.ILLUMINATED_CABINET_BLOCK, new FabricItemSettings()),
            "illuminated_cabinet_block"
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
            CABINET_ITEM_VARIANTS[i] = register(
                    new DoubleVerticallyAttachableBlockItem(
                            BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i],
                            BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i],
                            new FabricItemSettings()
                    ),
                    "cabinet_" + material.getSafeName()
            );
            ILLUMINATED_CABINET_ITEM_VARIANTS[i] = register(
                    new DoubleVerticallyAttachableBlockItem(
                            BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i],
                            BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i],
                            new FabricItemSettings()
                    ),
                    "illuminated_cabinet_" + material.getSafeName()
            );
            ++i;
        }

        // CANDLESTICK BETA
        if (ModConfig.getEnableCandlestickBeta()) {
            CANDLESTICK_ITEM_VARIANTS = new Item[ActiveGenerationData.simpleCandlestickMaterials.size()];
            i = 0;
            for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
                CANDLESTICK_ITEM_VARIANTS[i] = register(
                        "candlestick_" + material.getSafeName(),
                        new ModVerticallyAttachableBlockItem(
                                BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS[i],
                                BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS[i],
                                new FabricItemSettings(),
                                Direction.DOWN
                        )
                );
                ++i;
            }
            RUSTABLE_CANDLESTICK_ITEM_VARIANTS = new Item[ActiveGenerationData.rustingCandlestickMaterials.length][];
            i = 0;
            for (SizedIterable<BlockMaterial> materialSet : ActiveGenerationData.rustingCandlestickMaterials) {
                RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i] = new Item[materialSet.size()];
                int j = 0;
                for (BlockMaterial material : materialSet) {
                    RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][j] = register(
                            "candlestick_" + material.getSafeName(),
                            new ModVerticallyAttachableBlockItem(
                                    BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[i][j],
                                    BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[i][j],
                                    new FabricItemSettings(),
                                    Direction.DOWN
                            )
                    );
                    if (RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][j] == null) throw new NullPointerException();
                    ++j;
                }
                ++i;
            }
        }
        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            COLOURED_TORCH_ITEM_VARIANTS = new Item[BlockRegistry.COLOURED_TORCH_VARIANTS.length];
            i = 0;
            for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
                COLOURED_TORCH_ITEM_VARIANTS[i] = register(
                        "coloured_torch_" + material.getRawName(),
                        new ModVerticallyAttachableBlockItem(
                                BlockRegistry.COLOURED_TORCH_VARIANTS[i],
                                BlockRegistry.COLOURED_WALL_TORCH_VARIANTS[i],
                                new Item.Settings(),
                                Direction.DOWN
                        )
                );
                ++i;
            }
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
