package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION >= 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
#else
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
#endif

public class IlluminatedCabinetBlockEntity extends CabinetBlockEntity {

    public IlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
