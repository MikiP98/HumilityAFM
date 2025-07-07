package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class FloorIlluminatedCabinetBlock extends FloorCabinetBlock {
    public static final Settings defaultSettings = getDefaultSettings().luminance((ignored) -> 2);

    public FloorIlluminatedCabinetBlock(Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FloorIlluminatedCabinetBlockEntity(pos, state);
    }
}
