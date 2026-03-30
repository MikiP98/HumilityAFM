package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class IlluminatedCabinetBlock extends CabinetBlock {
    public static final Supplier<Properties> defaultSettingsSupplier =
            () -> CabinetBlock.defaultSettingsSupplier.get().lightLevel((ignored) -> 2);
    public static final Properties defaultSettings = defaultSettingsSupplier.get();

    public IlluminatedCabinetBlock(Properties settings) { super(settings); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
