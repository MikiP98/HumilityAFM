package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.LEDBlock;
import io.github.mikip98.helpers.MainHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.MOD_ID;

public class LEDHelper {
    static String[] colors = MainHelper.getWoolTypes();

    public static String[] LEDBlockVariantsNames;

    public static Block[] LEDBlockVariants;
    public static Item[] LEDBlockItemVariants;

    public static void init() {
        // Create LED block variants
        short LEDBlockVariantsCount = (short) (MainHelper.getWoolTypes().length);

        LEDBlockVariantsNames = new String[LEDBlockVariantsCount];

        LEDBlockVariants = new Block[LEDBlockVariantsCount];
        LEDBlockItemVariants = new Item[LEDBlockVariantsCount];

        FabricBlockSettings LEDBlockSettings = FabricBlockSettings.create().strength(0.5f).nonOpaque();

        short i = 0;
        for (String color : colors) {
            LEDBlockVariantsNames[i] = color;
            //LOGGER.info("Creating LED block variant: " + LEDBlockVariantsNames[i]);
            // Create LED block variant
            LEDBlockVariants[i] = new LEDBlock(LEDBlockSettings);
            // Create LED block variant item
            LEDBlockItemVariants[i] = new BlockItem(LEDBlockVariants[i], new FabricItemSettings());

            ++i;
        }
    }

    public static void registerLEDBlockVariants() {
        short i = 0;
        for (String color : colors) {
            String LEDBlockVariantName = color + "_led";
            //LOGGER.info("Registering LED block variant: " + LEDBlockVariantsNames[i]);
            // Register LED block variant
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, LEDBlockVariantName), LEDBlockVariants[i]);
            // Register LED block variant item
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, LEDBlockVariantName), LEDBlockItemVariants[i]);

            ++i;
        }
    }
}
