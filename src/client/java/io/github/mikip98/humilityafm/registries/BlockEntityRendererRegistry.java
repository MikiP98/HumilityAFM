package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.block_entity_renderers.LightStripBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.CabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.FloorCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.FloorIlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.mod_support.ModSupportManager;
import io.github.mikip98.humilityafm.mod_support.SupportedMods;
import io.mikip98.humilityval.client.registries.BlockEntityRendererRegistryUtil;

public class BlockEntityRendererRegistry extends BlockEntityRendererRegistryUtil {
    public static void register() {
        register(BlockEntityRegistry.CABINET_BLOCK_ENTITY, CabinetBlockEntityRenderer::new);
        register(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, IlluminatedCabinetBlockEntityRenderer::new);
        register(BlockEntityRegistry.FLOOR_CABINET_BLOCK_ENTITY, FloorCabinetBlockEntityRenderer::new);
        register(BlockEntityRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY, FloorIlluminatedCabinetBlockEntityRenderer::new);

        if (ModConfig.illuminatedCabinetBlockBrightening) {
            IlluminatedCabinetBlockEntityRenderer.enableBrightening();
            FloorIlluminatedCabinetBlockEntityRenderer.enableBrightening();
        }

        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            register(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, LightStripBlockEntityRenderer::new);

            if (ModConfig.enableLightStripBrightening && !ModSupportManager.isModLoaded(SupportedMods.SHIMMER)) {
                LightStripBlockEntityRenderer.enableBrightening();
            }
        }
    }
}
