package io.github.mikip98.content.blocks.cabinet;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import io.github.mikip98.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;

public class IlluminatedCabinetBlock extends CabinetBlock {

    public IlluminatedCabinetBlock(Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
