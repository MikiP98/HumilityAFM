package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightStripBlockEntity extends BlockEntity {
    public LightStripBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, pos, state);
    }
}
