package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class FloorIlluminatedCabinetBlockEntity extends FloorCabinetBlockEntity {

    public FloorIlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
