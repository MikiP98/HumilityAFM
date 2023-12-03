package io.github.mikip98;

import io.github.mikip98.config.ModConfig;
import io.github.mikip98.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.helpers.CabinetBlockHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import io.github.mikip98.content.blockentities.cabinetBlock.CabinetBlockEntityRenderer;
import io.github.mikip98.content.blockentities.LEDBlockEntityRenderer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.MOD_ID;

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


		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(asId("CabinetVanillaResourcepacksCompatibility"), container, ResourcePackActivationType.NORMAL);
		});
	}

	public static Identifier asId(String path) {
		return new Identifier(MOD_ID, path);
	}
}