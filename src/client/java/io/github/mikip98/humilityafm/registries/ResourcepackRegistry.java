package io.github.mikip98.humilityafm.registries;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;
import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ResourcepackRegistry {
    public static void register() {
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            // RESOURCE PACKS
            ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet"), container, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet_plus_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(getId("cabinet_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(getId("low_quality_cabinet"), container, ResourcePackActivationType.NORMAL);
            // DATA PACKS
            ResourceManagerHelper.registerBuiltinResourcePack(getId("alternate_wooden_mosaic_recipies"), container, ResourcePackActivationType.NORMAL);
        });
    }
}
