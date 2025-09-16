package io.github.mikip98.humilityafm.registries;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;
import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ResourcepackRegistry {
    public static void register() {
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            // ......... RESOURCE PACKS .........
            // Cabinets
            register("3d_cabinet", container, "3D Cabinets", ResourcePackActivationType.NORMAL);
            register("3d_cabinet_plus_vanilla_rp_compat", container, "3D Cabinets + Vanilla RP Compat Improvement", ResourcePackActivationType.NORMAL);
            register("cabinet_vanilla_rp_compat", container, "Cabinets Vanilla RP Compat Improvement", ResourcePackActivationType.NORMAL);
            register("low_quality_cabinet", container, "Low Quality Cabinets", ResourcePackActivationType.NORMAL);
            // Jack o'Lanterns
            register("jack_o_lanterns_vanilla_rp_compat", container, "Jack o'Lanterns Vanilla RP Compat Improvement", ResourcePackActivationType.NORMAL);
            // ......... DATA PACKS .........
            register("alternate_wooden_mosaic_recipies", container, "Alternate Wooden Mosaic Recipies", ResourcePackActivationType.NORMAL);
        });
    }
    // TODO: Replace with Text.translatable();
    protected static void register(String id, ModContainer container, String name, ResourcePackActivationType activationType) {
        ResourceManagerHelper.registerBuiltinResourcePack(getId(id), container, Text.literal(name), activationType);
    }
}
