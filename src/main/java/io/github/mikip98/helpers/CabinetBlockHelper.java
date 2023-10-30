package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.MOD_ID;

public class CabinetBlockHelper {
    public CabinetBlockHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCabinetBlockVariants()\" instead!");
    }

    private static final float CabinetBlockStrength = 2.0f;
    private static final FabricBlockSettings CabinetBlockSettings = FabricBlockSettings.create().strength(CabinetBlockStrength).requiresTool().nonOpaque().sounds(BlockSoundGroup.WOOD);
    private static final FabricBlockSettings IlluminatedCabinetBlockSettings = FabricBlockSettings.create().strength(CabinetBlockStrength).requiresTool().nonOpaque().sounds(BlockSoundGroup.WOOD).luminance(2);

    public static String[] cabinetBlockVariantsNames;

    public static Block[] cabinetBlockVariants;
    public static Item[] cabinetBlockItemVariants;

    public static Block[] illuminatedCabinetBlockVariants;
    public static Item[] illuminatedCabinetBlockItemVariants;


    public static void init() {
        //Create cabinet variants
        short cabinetBlockVariantsCount = (short) (MainHelper.getWoodTypes().length * MainHelper.getWoolTypes().length);

        cabinetBlockVariantsNames = new String[cabinetBlockVariantsCount];

        cabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        cabinetBlockItemVariants = new Item[cabinetBlockVariantsCount];

        illuminatedCabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        illuminatedCabinetBlockItemVariants = new Item[cabinetBlockVariantsCount];

        short i = 0;
        for (String woodType : MainHelper.getWoodTypes()) {
            for (String woolType : MainHelper.getWoolTypes()) {
                String cabinetBlockVariantName = woodType + "_" + woolType;
                cabinetBlockVariantsNames[i] = cabinetBlockVariantName;
                //LOGGER.info("Creating cabinet block variant: " + cabinetBlockVariantsNames[i]);
                // Create cabinet block variant
                cabinetBlockVariants[i] = new CabinetBlock(CabinetBlockSettings);
                // Create cabinet block variant item
                cabinetBlockItemVariants[i] = new BlockItem(cabinetBlockVariants[i], new FabricItemSettings());

                // Create illuminated cabinet block variant
                illuminatedCabinetBlockVariants[i] = new IlluminatedCabinetBlock(IlluminatedCabinetBlockSettings);
                // Create illuminated cabinet block variant item
                illuminatedCabinetBlockItemVariants[i] = new BlockItem(illuminatedCabinetBlockVariants[i], new FabricItemSettings());

                ++i;
            }
        }
    }

    public static void registerCabinetBlockVariants() {
        //Register cabinet variants
        int i = 0;
        for (Block cabinetBlockVariant : cabinetBlockVariants) {
            //LOGGER.info("Registering cabinet block variant: " + cabinetBlockVariantsNames[i]);
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "cabinet_block_" + cabinetBlockVariantsNames[i]), cabinetBlockVariant);
            ++i;
        }
        i = 0;
        for (Item cabinetBlockItemVariant : cabinetBlockItemVariants) {
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, "cabinet_block_" + cabinetBlockVariantsNames[i]), cabinetBlockItemVariant);
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> content.add(cabinetBlockItemVariant));
            ++i;
        }

        i = 0;
        for (Block illuminatedCabinetBlockVariant : illuminatedCabinetBlockVariants) {
            //LOGGER.info("Registering illuminated cabinet block variant: " + cabinetBlockVariantsNames[i]);
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "illuminated_cabinet_block_" + cabinetBlockVariantsNames[i]), illuminatedCabinetBlockVariant);
            ++i;
        }
        i = 0;
        for (Item illuminatedCabinetBlockItemVariant : illuminatedCabinetBlockItemVariants) {
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, "illuminated_cabinet_block_" + cabinetBlockVariantsNames[i]), illuminatedCabinetBlockItemVariant);
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> content.add(illuminatedCabinetBlockItemVariant));
            ++i;
        }

        // Making cabinets flammable
        // (3 x 20 + 60) / 5  TODO: Make those a config option
        // (60 + 60) / 5 = 24 -> flammability
        // (15 + 30) / 5 = 9 -> fire spreading speed
        short burnTime = 24;
        short spreadSpeed = 9;

        for (Block cabinetBlockVariant : cabinetBlockVariants) {
            FlammableBlockRegistry.getDefaultInstance().add(cabinetBlockVariant, burnTime, spreadSpeed);
        }
        for (Block illuminatedCabinetBlockVariant : illuminatedCabinetBlockVariants) {
            FlammableBlockRegistry.getDefaultInstance().add(illuminatedCabinetBlockVariant, burnTime, spreadSpeed);
        }
    }
}
