package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.helpers.MainHelper;
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
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class CabinetBlockGenerator {
    public CabinetBlockGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCabinetBlockVariants()\" instead!");
    }

    public static Map<String, Short> woodTypeToStartIndex;
    public static String[] cabinetBlockVariantsNames;

    // Vanilla
    public static Block[] cabinetBlockVariants;
    public static Item[] cabinetBlockItemVariants;

    public static Block[] illuminatedCabinetBlockVariants;
    public static Item[] illuminatedCabinetBlockItemVariants;

    // Modded
    public static Block[] moddedCabinetBlockVariants;
    public static Item[] moddedCabinetBlockItemVariants;

    public static Block[] moddedIlluminatedCabinetBlockVariants;
    public static Item[] moddedIlluminatedCabinetBlockItemVariants;


    private static void createCabinetBlockVariant(
            String woodType, String woolType,
            short i,
            FabricBlockSettings CabinetBlockSettings, FabricBlockSettings IlluminatedCabinetBlockSettings
    ) {
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
    }


    private static void putVariantInModded(short i, short j) {
        moddedCabinetBlockVariants[j] = cabinetBlockVariants[i];
        moddedCabinetBlockItemVariants[j] = cabinetBlockItemVariants[i];
        moddedIlluminatedCabinetBlockVariants[j] = illuminatedCabinetBlockVariants[i];
        moddedIlluminatedCabinetBlockItemVariants[j] = illuminatedCabinetBlockItemVariants[i];
    }


    public static void init() {
        final FabricBlockSettings CabinetBlockSettings = CabinetBlock.defaultSettings;
        final FabricBlockSettings IlluminatedCabinetBlockSettings = FabricBlockSettings.copyOf(CabinetBlock.defaultSettings).luminance(2);
        // TODO: change the map color parameter based on the current wood type

        //Create cabinet variants
        short cabinetBlockVariantsCount = (short) (MainHelper.vanillaWoodTypes.length * MainHelper.vanillaWoolTypes.length);
        if (ModConfig.betterNetherDetected) {
            cabinetBlockVariantsCount += (short) (MainHelper.betterNetherWoodTypes.length * MainHelper.vanillaWoolTypes.length);
        }

        woodTypeToStartIndex = new HashMap<>();
        cabinetBlockVariantsNames = new String[cabinetBlockVariantsCount];

        cabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        cabinetBlockItemVariants = new Item[cabinetBlockVariantsCount];

        illuminatedCabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        illuminatedCabinetBlockItemVariants = new Item[cabinetBlockVariantsCount];

        short i = 0;
        for (String woodType : MainHelper.vanillaWoodTypes) {
            woodTypeToStartIndex.put(woodType, i);
            for (String woolType : MainHelper.vanillaWoolTypes) {
                createCabinetBlockVariant(woodType, woolType, i, CabinetBlockSettings, IlluminatedCabinetBlockSettings);
                ++i;
            }
        }
        if (ModConfig.betterNetherDetected) {
            short moddedCabinetCount = (short) (MainHelper.betterNetherWoodTypes.length * MainHelper.vanillaWoolTypes.length);

            moddedCabinetBlockVariants = new Block[moddedCabinetCount];
            moddedCabinetBlockItemVariants = new Item[moddedCabinetCount];
            moddedIlluminatedCabinetBlockVariants = new Block[moddedCabinetCount];
            moddedIlluminatedCabinetBlockItemVariants = new Item[moddedCabinetCount];

            short j = 0;
            for (String woodType : MainHelper.betterNetherWoodTypes) {
                for (String woolType : MainHelper.vanillaWoolTypes) {
                    createCabinetBlockVariant(woodType, woolType, i, CabinetBlockSettings, IlluminatedCabinetBlockSettings);
                    putVariantInModded(i, j);

                    ++i;
                    ++j;
                }
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
        short burnTime;
        short spreadSpeed;
        burnTime = (short) ModConfig.cabinetBlockBurnTime;
        spreadSpeed = (short) ModConfig.cabinetBlockFireSpread;

        for (Block cabinetBlockVariant : cabinetBlockVariants) {
            FlammableBlockRegistry.getDefaultInstance().add(cabinetBlockVariant, burnTime, spreadSpeed);
        }
        for (Block illuminatedCabinetBlockVariant : illuminatedCabinetBlockVariants) {
            FlammableBlockRegistry.getDefaultInstance().add(illuminatedCabinetBlockVariant, burnTime, spreadSpeed);
        }
    }
}
