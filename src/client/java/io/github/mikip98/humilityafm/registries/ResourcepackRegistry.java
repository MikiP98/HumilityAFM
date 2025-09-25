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
            register("cabinet_labpbr_full", container, "Cabinet LabPBR Data", ResourcePackActivationType.DEFAULT_ENABLED);
            // Coloured Torches
            register("coloured_torch_optifine_emission", container, "Coloured Torch Optifine Emission", ResourcePackActivationType.NORMAL);
            register("coloured_torch_labpbr_sharp", container, "Coloured Torch LabPBR (sharp)", ResourcePackActivationType.DEFAULT_ENABLED);
            register("coloured_torch_labpbr_smooth", container, "Coloured Torch LabPBR (smooth)", ResourcePackActivationType.NORMAL);
            // Jack o'Lanterns
            register("jack_o_lantern_vanilla_rp_compat", container, "Jack o'Lanterns Vanilla RP Compat Improvement", ResourcePackActivationType.NORMAL);
            register("special_jack_o_lanterns_optifine_emission", container, "Special Jack o'Lanterns Optifine Emission", ResourcePackActivationType.NORMAL);
            register("coloured_jack_o_lanterns_optifine_emission", container, "Coloured Jack o'Lanterns Optifine Emission", ResourcePackActivationType.NORMAL);
            register("special_jack_o_lanterns_labpbr_emission_face", container, "Special Jack o'Lanterns LabPBR Emission Sharp Face", ResourcePackActivationType.DEFAULT_ENABLED);
            register("coloured_jack_o_lanterns_labpbr_emission_face", container, "Coloured Jack o'Lanterns LabPBR Emission Sharp Face", ResourcePackActivationType.DEFAULT_ENABLED);
            register("special_jack_o_lanterns_labpbr_emission_smooth", container, "Special Jack o'Lanterns LabPBR Smooth", ResourcePackActivationType.NORMAL);
            register("coloured_jack_o_lanterns_labpbr_emission_smooth", container, "Coloured Jack o'Lanterns LabPBR Smooth", ResourcePackActivationType.NORMAL);

            // ......... DATA PACKS .........
            register("alternate_wooden_mosaic_recipies", container, "Alternate Wooden Mosaic Recipies", ResourcePackActivationType.NORMAL);
        });
    }
    // TODO: Consider making a RegistryHelper class to skip passing the 'container' every time
    // TODO: Replace with Text.translatable();
    protected static void register(String id, ModContainer container, String name, ResourcePackActivationType activationType) {
        ResourceManagerHelper.registerBuiltinResourcePack(getId(id), container, Text.literal(name), activationType);
    }
}
