package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FloorIlluminatedCabinetBlock extends FloorCabinetBlock {
    public static final Properties defaultSettings = IlluminatedCabinetBlock.defaultSettingsSupplier.get();

    public FloorIlluminatedCabinetBlock(Properties settings) { super(settings); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FloorIlluminatedCabinetBlockEntity(pos, state);
    }
}
