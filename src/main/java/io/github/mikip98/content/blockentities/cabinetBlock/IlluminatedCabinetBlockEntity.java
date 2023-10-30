package io.github.mikip98.content.blockentities.cabinetBlock;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import io.github.mikip98.HumilityAFM;

public class IlluminatedCabinetBlockEntity extends CabinetBlockEntity{

    public IlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(HumilityAFM.ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
    }
}
