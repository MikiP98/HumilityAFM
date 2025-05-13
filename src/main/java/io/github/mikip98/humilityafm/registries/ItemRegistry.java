package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Arrays;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;
import static io.github.mikip98.humilityafm.registries.ItemGroupRegistry.putIntoItemGroup;

public class ItemRegistry {
    public static Item[] glowingPowderVariants = Arrays.stream(GenerationData.vanillaColorPallet).map(color -> Registry.register(Registries.ITEM, getId("glowing_powder_" + color), new Item(new FabricItemSettings()))).toArray(Item[]::new);

    public static void register() {
        putIntoItemGroup(glowingPowderVariants, ItemGroups.COLORED_BLOCKS);
    }
}
