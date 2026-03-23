package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
#if MC_VERSION < 12106
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
#else
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
#endif
import net.minecraft.block.Block;
#if MC_VERSION >= 12106
import net.minecraft.client.render.BlockRenderLayer;
#else
import net.minecraft.client.render.RenderLayer;
#endif

public class RenderLayerRegistry {
    #if MC_VERSION < 12106
    protected static final RenderLayer cabinetRenderLayer = ModConfig.transparentCabinetBlocks ? RenderLayer.getTranslucent() : RenderLayer.getCutoutMipped();
    #else
    protected static final BlockRenderLayer cabinetRenderLayer = ModConfig.transparentCabinetBlocks ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT_MIPPED;
    #endif
    public static void register() {
        applyCabinetRenderLayer(BlockRegistry.CABINET_BLOCK);
        applyCabinetRenderLayer(BlockRegistry.ILLUMINATED_CABINET_BLOCK);
        applyCabinetRenderLayer(BlockRegistry.FLOOR_CABINET_BLOCK);
        applyCabinetRenderLayer(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK);
        applyCabinetRenderLayer(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS);
        applyCabinetRenderLayer(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS);
        applyCabinetRenderLayer(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS);
        applyCabinetRenderLayer(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS);
    }
    protected static void applyCabinetRenderLayer(Block... blocks) {
        applyRenderLayer(cabinetRenderLayer, blocks);
    }
    #if MC_VERSION < 12106
    protected static void applyRenderLayer(RenderLayer renderLayer, Block... blocks) {
        BlockRenderLayerMap.INSTANCE.putBlocks(renderLayer, blocks);
    }
    #else
    protected static void applyRenderLayer(BlockRenderLayer renderLayer, Block... blocks) {
        BlockRenderLayerMap.putBlocks(renderLayer, blocks);
    }
    #endif
}
