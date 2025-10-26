package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.BlockRenderLayer;

public class RenderLayerRegistry {
    protected static final BlockRenderLayer cabinetRenderLayer = ModConfig.transparentCabinetBlocks ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT_MIPPED;
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
    protected static void applyRenderLayer(BlockRenderLayer renderLayer, Block... blocks) {
        BlockRenderLayerMap.putBlocks(renderLayer, blocks);
    }
}
