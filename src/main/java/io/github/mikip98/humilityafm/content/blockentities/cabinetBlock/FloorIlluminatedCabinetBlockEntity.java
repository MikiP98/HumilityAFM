package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION >= 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
#else
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
#endif

public class FloorIlluminatedCabinetBlockEntity extends FloorCabinetBlockEntity {
    public FloorIlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
