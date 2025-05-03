package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.HumilityAFM;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LEDBlockEntity extends BlockEntity {
    public LEDBlockEntity(BlockPos pos, BlockState state) {
        super(HumilityAFM.LED_BLOCK_ENTITY, pos, state);
    }
}
