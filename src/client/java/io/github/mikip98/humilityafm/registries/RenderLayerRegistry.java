package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.mikip98.humilityval.client.registries.BlockRenderLayerRegistryUtil;
import net.minecraft.world.level.block.Block;

public class RenderLayerRegistry extends BlockRenderLayerRegistryUtil {
    protected static final BlockConsumer putBlocksIntoCabinetRenderLayer = ModConfig.translucentCabinetBlocks ?
            BlockRenderLayerRegistryUtil::putBlocksInTranslucent : BlockRenderLayerRegistryUtil::putBlocksInCutoutMipped;

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
        putBlocksIntoCabinetRenderLayer.accept(blocks);
    }
}