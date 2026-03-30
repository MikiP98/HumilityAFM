package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FloorIlluminatedCabinetBlockEntity extends FloorCabinetBlockEntity {
    public FloorIlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
