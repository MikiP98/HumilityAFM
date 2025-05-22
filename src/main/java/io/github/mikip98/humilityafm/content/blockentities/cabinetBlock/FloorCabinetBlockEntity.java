package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class FloorCabinetBlockEntity extends CabinetBlockEntity {

    public FloorCabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public FloorCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_CABINET_BLOCK_ENTITY, pos, state);
    }
}
