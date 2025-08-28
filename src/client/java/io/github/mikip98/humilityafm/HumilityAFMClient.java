package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.mod_support.ModSupportManager;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
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

import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;
import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(BlockEntityRegistry.CABINET_BLOCK_ENTITY, CabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, IlluminatedCabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BlockEntityRegistry.FLOOR_CABINET_BLOCK_ENTITY, FloorCabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BlockEntityRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY, FloorIlluminatedCabinetBlockEntityRenderer::new);

		RenderLayer renderLayer = ModConfig.transparentCabinetBlocks ? RenderLayer.getTranslucent() : RenderLayer.getCutout();

		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.CABINET_BLOCK, renderLayer);
		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ILLUMINATED_CABINET_BLOCK, renderLayer);
		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.FLOOR_CABINET_BLOCK, renderLayer);
		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, renderLayer);

		Consumer<Block[]> applyCabinetRenderLayer = (blocks) -> BlockRenderLayerMap.INSTANCE.putBlocks(renderLayer, blocks);
		applyCabinetRenderLayer.accept(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS);
		applyCabinetRenderLayer.accept(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS);
		applyCabinetRenderLayer.accept(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS);
		applyCabinetRenderLayer.accept(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS);

		// Light Strip variants
		if (ModConfig.getEnableColouredFeatureSetBeta()) {
			BlockEntityRendererFactories.register(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, LightStripBlockEntityRenderer::new);
			if (ModConfig.enableLightStripBrightening && !ModSupportManager.isModLoaded(SupportedMods.SHIMMER))
				LightStripBlockEntityRenderer.enableBrightening();
		}

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("3d_cabinet_plus_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("cabinet_vanilla_rp_compat"), container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(getId("low_quality_cabinet"), container, ResourcePackActivationType.NORMAL);
		});
	}
}