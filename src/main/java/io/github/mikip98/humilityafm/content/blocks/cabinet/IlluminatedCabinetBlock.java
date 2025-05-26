package io.github.mikip98.humilityafm.content.blocks.cabinet;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;

public class IlluminatedCabinetBlock extends CabinetBlock {
    public static final AbstractBlock.Settings defaultSettings = getDefaultSettings().luminance((ignored) -> 2);

    public IlluminatedCabinetBlock() { this(defaultSettings); }
    public IlluminatedCabinetBlock(Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
