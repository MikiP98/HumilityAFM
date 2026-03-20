package io.github.mikip98.humilityafm.content.blocks.cabinet;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;

import java.util.function.Supplier;

public class IlluminatedCabinetBlock extends CabinetBlock {
    public static final Supplier<Settings> defaultSettingsSupplier = () -> CabinetBlock.defaultSettingsSupplier.get().luminance((ignored) -> 2);
    public static final Settings defaultSettings = defaultSettingsSupplier.get();

    public IlluminatedCabinetBlock(Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
