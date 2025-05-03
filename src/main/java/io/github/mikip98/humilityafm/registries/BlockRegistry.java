package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.leds.LEDBlock;
import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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

    // LEDs
    public static Block LED_BLOCK;


    public static void register() {
        // Register Cabinets
        registerWithItem(CABINET_BLOCK, "cabinet_block");
        registerWithItem(ILLUMINATED_CABINET_BLOCK, "illuminated_cabinet_block");
        // Register Stairs
        registerWithItem(OUTER_STAIRS, "outer_stairs");
        registerWithItem(INNER_STAIRS, "inner_stairs");
        // Register Wooden Mosaic
        registerWithItem(WOODEN_MOSAIC, "wooden_mosaic");
        // Register LED
        if (ModConfig.enableLEDs) {
            LED_BLOCK = new LEDBlock(FabricBlockSettings.create().strength(0.5f).nonOpaque().sounds(BlockSoundGroup.GLASS).luminance(10));
            registerWithItem(LED_BLOCK, "led");
        }
    }


    protected static void registerWithItem(Block block, String name) {
        Identifier id = getId(name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
    }
}
