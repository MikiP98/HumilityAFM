package io.github.mikip98.humilityafm.content.blocks.leds;

import io.github.mikip98.humilityafm.content.blockentities.LEDBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LEDStripBlockWithEntity extends LEDStripBlock implements BlockEntityProvider {
    public LEDStripBlockWithEntity() { super(); }
    public LEDStripBlockWithEntity(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LEDBlockEntity(pos, state);
    }
}
