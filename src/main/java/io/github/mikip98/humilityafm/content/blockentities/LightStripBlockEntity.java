package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LightStripBlockEntity extends BlockEntity {
    public LightStripBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, pos, state);
    }
}
