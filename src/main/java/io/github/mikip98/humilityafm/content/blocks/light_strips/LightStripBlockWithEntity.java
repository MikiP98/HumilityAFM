package io.github.mikip98.humilityafm.content.blocks.light_strips;

import io.github.mikip98.humilityafm.content.blockentities.LEDBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LightStripBlockWithEntity extends LightStripBlock implements BlockEntityProvider {
    public LightStripBlockWithEntity() { super(); }
    public LightStripBlockWithEntity(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LEDBlockEntity(pos, state);
    }
}
