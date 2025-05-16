package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class IlluminatedCabinetBlockEntity extends CabinetBlockEntity {

    public IlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
