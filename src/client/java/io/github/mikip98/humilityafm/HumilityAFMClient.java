package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntityRenderer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;
import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		BlockEntityRendererFactories.register(BlockEntityRegistry.CABINET_BLOCK_ENTITY, CabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, IlluminatedCabinetBlockEntityRenderer::new);

		RenderLayer renderLayer = ModConfig.TransparentCabinetBlocks ? RenderLayer.getTranslucent() : RenderLayer.getCutout();

		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.CABINET_BLOCK, renderLayer);
		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ILLUMINATED_CABINET_BLOCK, renderLayer);

		for (Block cabinetBlockVariant : CabinetBlockGenerator.cabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(cabinetBlockVariant, renderLayer);
		}
		for (Block illuminatedCabinetBlockVariant : CabinetBlockGenerator.illuminatedCabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(illuminatedCabinetBlockVariant, renderLayer);
		}

		// LED block variants
		if (ModConfig.enableLightStrips) {
			BlockEntityRendererFactories.register(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, LightStripBlockEntityRenderer::new);
			if (ModConfig.enableLightStripBrightening && !ModConfig.shimmerDetected) LightStripBlockEntityRenderer.enableBrightening();
		}

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet_plus_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("cabinet_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("low_quality_cabinet"), container, ResourcePackActivationType.NORMAL);
		});
	}
}