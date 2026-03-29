package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION < 260000
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
#endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
#if MC_VERSION >= 260000
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
#endif

import java.util.function.Supplier;

public class IlluminatedCabinetBlock extends CabinetBlock {
    public static final Supplier<#if MC_VERSION < 260000 Settings #else Properties #endif>
            defaultSettingsSupplier = () -> CabinetBlock.defaultSettingsSupplier.get().#if MC_VERSION < 260000 luminance #else lightLevel #endif((ignored) -> 2);
    public static final #if MC_VERSION < 260000 Settings #else Properties #endif defaultSettings = defaultSettingsSupplier.get();

    public IlluminatedCabinetBlock(#if MC_VERSION < 260000 Settings #else Properties #endif settings) { super(settings); }

    @Override
    #if MC_VERSION < 260000
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    #else
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    #endif
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
