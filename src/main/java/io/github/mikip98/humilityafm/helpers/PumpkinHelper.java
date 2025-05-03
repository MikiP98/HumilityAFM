package io.github.mikip98.humilityafm.helpers;

import io.github.mikip98.humilityafm.content.blocks.JustHorizontalFacingBlock;
import io.github.mikip98.humilityafm.util.data_types.Torch;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class PumpkinHelper {
    public PumpkinHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerPumpkins()\" instead!");
    }

    private static final Torch[] torches = {new Torch("redstone", (byte) 7), new Torch("soul", (byte) 8)};

    public static String[] PumpkinsNames = new String[torches.length];

    public static Block[] PumpkinsVariants = new Block[torches.length];
    public static Item[] PumpkinsItemVariants = new Item[torches.length];

    public static void init() {
        Settings PumpkinSettings = FabricBlockSettings.copy(Blocks.JACK_O_LANTERN);
        for (int i = 0; i < torches.length; i++) {
            PumpkinsNames[i] = "jack_o_lantern_" + torches[i].type;

            int finalI = i;
            Settings currentSettings = PumpkinSettings.luminance(value -> torches[finalI].lightLevel);
            PumpkinsVariants[i] = new JustHorizontalFacingBlock(currentSettings);
            PumpkinsItemVariants[i] = new BlockItem(PumpkinsVariants[i], new FabricItemSettings());
        }
        if (Math.random() < 0.05) {
            printPumpkin();
        }
    }

    public static void registerPumpkins() {
        for (int i = 0; i < PumpkinsVariants.length; i++) {
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, PumpkinsNames[i]), PumpkinsVariants[i]);
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, PumpkinsNames[i]), PumpkinsItemVariants[i]);
        }
    }

    public static void printPumpkin() {
        System.out.println("  ___  ");
        System.out.println(" / _ \\ ");
        System.out.println("| | | |");
        System.out.println("| |_| |");
        System.out.println(" \\___/ ");
    }
}
