package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockRegistry {

    // Cabinet blocks
    public static final CabinetBlock CABINET_BLOCK = new CabinetBlock();
    public static final IlluminatedCabinetBlock ILLUMINATED_CABINET_BLOCK = new IlluminatedCabinetBlock();

    // Stairs
    private static final float WoodenStairsBlockStrength = 2.0f;
    private static final FabricBlockSettings StairsBlockSettings = FabricBlockSettings.create().strength(WoodenStairsBlockStrength).requiresTool();
    public static final Block OUTER_STAIRS = new OuterStairs(StairsBlockSettings);
    public static final Block INNER_STAIRS = new InnerStairs(StairsBlockSettings);

    // Wooden mosaics
    private static final float WoodenMosaicStrength = 3.0f * 1.5f;
    private static final FabricBlockSettings WoodenMosaicSettings = FabricBlockSettings.create().strength(WoodenMosaicStrength).requiresTool().sounds(BlockSoundGroup.WOOD);
    public static final Block WOODEN_MOSAIC = new Block(WoodenMosaicSettings);


    public static void register() {
        // Test blocks
        // Register Cabinets
        registerWithItem(CABINET_BLOCK, "cabinet_block");
        registerWithItem(ILLUMINATED_CABINET_BLOCK, "illuminated_cabinet_block");
        // Register Stairs
        registerWithItem(OUTER_STAIRS, "outer_stairs");
        registerWithItem(INNER_STAIRS, "inner_stairs");
        // Register Wooden Mosaic
        registerWithItem(WOODEN_MOSAIC, "wooden_mosaic");

        // Real blocks
        // Register cabinets
        CabinetBlockGenerator.cabinetBlockItemVariants = registerArrayWithItems(
                CabinetBlockGenerator.cabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "cabinet_block_"
        );
        putIntoItemGroup(CabinetBlockGenerator.cabinetBlockItemVariants, ItemGroups.COLORED_BLOCKS);
        registerFlammable(CabinetBlockGenerator.cabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
        // Register illuminated cabinets
        CabinetBlockGenerator.illuminatedCabinetBlockItemVariants = registerArrayWithItems(
                CabinetBlockGenerator.illuminatedCabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "illuminated_cabinet_block_"
        );
        putIntoItemGroup(CabinetBlockGenerator.illuminatedCabinetBlockItemVariants, ItemGroups.COLORED_BLOCKS);
        registerFlammable(CabinetBlockGenerator.illuminatedCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
    }


    protected static Item[] registerArrayWithItems(Block[] blocks, String[] names, String prefix) {
        Item[] items = new Item[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            items[i] = registerWithItem(blocks[i], prefix + names[i]);
        }
        return items;
    }
    protected static Item registerWithItem(Block block, String name) {
        Identifier id = getId(name);
        Registry.register(Registries.BLOCK, id, block);
        return Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
    }


    protected static void registerFlammable(Block[] blocks, int burnTime, int spreadSpeed) {
        for (Block block : blocks) {
            FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
        }
    }
//    protected static void registerFlammable(Block block, short burnTime, short spreadSpeed) {
//        FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
//    }


    protected static void putIntoItemGroup(Item[] items, RegistryKey<ItemGroup> group) {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            for (Item item : items) {
                content.add(item);
            }
        });
    }
//    protected static void putIntoItemGroup(Item item, RegistryKey<ItemGroup> group) {
//        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item));
//    }
}
