package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class IlluminatedCabinetBlock extends CabinetBlock {
    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<IlluminatedCabinetBlock> CODEC = simpleCodec(IlluminatedCabinetBlock::new);

    @Override
    protected @NotNull MapCodec<? extends IlluminatedCabinetBlock> codec() {
        return CODEC;
    }
    #endif

    public static final Supplier<Properties> defaultSettingsSupplier =
            () -> CabinetBlock.defaultSettingsSupplier.get().lightLevel((ignored) -> 2);
    public static final Properties defaultSettings = defaultSettingsSupplier.get();

    public IlluminatedCabinetBlock(Properties settings) { super(settings); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
