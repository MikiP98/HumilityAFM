package io.github.mikip98;

import io.github.mikip98.config.ModConfig;
import io.github.mikip98.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.helpers.CabinetBlockHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import io.github.mikip98.content.blockentities.cabinetBlock.CabinetBlockEntityRenderer;
import io.github.mikip98.content.blockentities.LEDBlockEntityRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		BlockRenderLayerMap.INSTANCE.putBlock(HumilityAFM.CABINET_BLOCK, RenderLayer.getCutout());
		// Replace `RenderLayer.getCutout()` with `RenderLayer.getTranslucent()` if you have a translucent texture.
		BlockRenderLayerMap.INSTANCE.putBlock(HumilityAFM.ILLUMINATED_CABINET_BLOCK, RenderLayer.getCutout());

		BlockEntityRendererFactories.register(HumilityAFM.CABINET_BLOCK_ENTITY, CabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(HumilityAFM.ILLUMINATED_CABINET_BLOCK_ENTITY, IlluminatedCabinetBlockEntityRenderer::new);

		for (Block cabinetBlockVariant : CabinetBlockHelper.cabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(cabinetBlockVariant, RenderLayer.getCutout());
		}
		for (Block illuminatedCabinetBlockVariant : CabinetBlockHelper.illuminatedCabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(illuminatedCabinetBlockVariant, RenderLayer.getCutout());
		}

		// LED block variants
		if (ModConfig.enableLEDs) {
			BlockEntityRendererFactories.register(HumilityAFM.LED_BLOCK_ENTITY, LEDBlockEntityRenderer::new);
		}
	}
}