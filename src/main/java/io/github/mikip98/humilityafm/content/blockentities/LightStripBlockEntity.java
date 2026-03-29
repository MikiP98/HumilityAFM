package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION > 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
#else
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
#endif

public class LightStripBlockEntity extends BlockEntity {
    public LightStripBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, pos, state);
    }
}
