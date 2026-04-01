package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class RenderLayerRegistry {
    #if MC_VERSION < 12106
    protected static final RenderType cabinetRenderLayer =
            ModConfig.transparentCabinetBlocks ? RenderType.translucent() : RenderType.cutoutMipped();
    #else
    protected static final ChunkSectionLayer cabinetRenderLayer =
            ModConfig.transparentCabinetBlocks ?
                    ChunkSectionLayer.TRANSLUCENT : #if MC_VERSION < 12111 ChunkSectionLayer.CUTOUT_MIPPED #else ChunkSectionLayer.CUTOUT #endif;
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
    protected static void applyRenderLayer(RenderType renderLayer, Block... blocks) {
        BlockRenderLayerMap.INSTANCE.putBlocks(renderLayer, blocks);
    }
    #else
    protected static void applyRenderLayer(ChunkSectionLayer renderLayer, Block... blocks) {
        BlockRenderLayerMap.putBlocks(renderLayer, blocks);
    }
    #endif
}
