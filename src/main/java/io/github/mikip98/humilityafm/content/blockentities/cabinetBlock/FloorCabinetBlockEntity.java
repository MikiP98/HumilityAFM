package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION >= 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
#else
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
#endif

public class FloorCabinetBlockEntity extends CabinetBlockEntity {

    public FloorCabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public FloorCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_CABINET_BLOCK_ENTITY, pos, state);
    }
}
