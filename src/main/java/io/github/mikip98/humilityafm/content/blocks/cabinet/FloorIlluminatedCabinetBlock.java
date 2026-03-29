package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
#if MC_VERSION >= 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
#else
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
#endif

public class FloorIlluminatedCabinetBlock extends FloorCabinetBlock {
    public static final #if MC_VERSION < 260000 Settings #else Properties #endif defaultSettings = IlluminatedCabinetBlock.defaultSettingsSupplier.get();

    public FloorIlluminatedCabinetBlock(#if MC_VERSION < 260000 Settings #else Properties #endif settings) { super(settings); }

    @Override
    #if MC_VERSION < 260000
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    #else
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    #endif
        return new FloorIlluminatedCabinetBlockEntity(pos, state);
    }
}
