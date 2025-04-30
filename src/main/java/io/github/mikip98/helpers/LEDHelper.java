package io.github.mikip98.helpers;

import io.github.mikip98.config.ModConfig;
import io.github.mikip98.content.blocks.leds.LEDStripBlock;
import io.github.mikip98.content.blocks.leds.LEDStripBlockWE;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.MOD_ID;

public class LEDHelper {
    public static String[] colors = MainHelper.vanillaWoolTypes;

    public static Item[] LEDPowderVariants;
    public static Block[] LEDBlockVariants;
    public static Item[] LEDBlockItemVariants;

    public static void init() {
        // Create LED block variants
        Block LEDFictionalBlock = new Block(FabricBlockSettings.create().strength(0.5f).sounds(BlockSoundGroup.GLASS).luminance(15));
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "led_fictional_block"), LEDFictionalBlock);

        short LEDBlockVariantsCount = (short) (colors.length);

        LEDPowderVariants = new Item[LEDBlockVariantsCount];

        LEDBlockVariants = new Block[LEDBlockVariantsCount];
        LEDBlockItemVariants = new Item[LEDBlockVariantsCount];

        FabricBlockSettings LEDBlockSettings = FabricBlockSettings.create().strength(0.5f).sounds(BlockSoundGroup.GLASS).luminance(9);

        short i = 0;
        for (String ignored : colors) {
            // Create LED powder variant
            LEDPowderVariants[i] = new Item(new FabricItemSettings());

            // Create LED block variant
            if (!ModConfig.enableLEDsBrightening || ModConfig.shimmerDetected) {
                LEDBlockVariants[i] = new LEDStripBlock(LEDFictionalBlock.getDefaultState(), LEDBlockSettings);
            } else {
                LEDBlockVariants[i] = new LEDStripBlockWE(LEDFictionalBlock.getDefaultState(), LEDBlockSettings);
            }

            // Create LED block variant item
            LEDBlockItemVariants[i] = new BlockItem(LEDBlockVariants[i], new FabricItemSettings());

            ++i;
        }
    }

    public static void registerLEDBlockVariants() {
        short i = 0;
        for (String color : colors) {
            String LEDPowderVariantName = "led_powder_" + color;
            // Register LED powder variant
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, LEDPowderVariantName), LEDPowderVariants[i]);

            String LEDBlockVariantName = "led_" + color;
            //LOGGER.info("Registering LED block variant: " + LEDBlockVariantsNames[i]);
            // Register LED block variant
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, LEDBlockVariantName), LEDBlockVariants[i]);
            // Register LED block variant item
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, LEDBlockVariantName), LEDBlockItemVariants[i]);

            ++i;
        }
    }
}
