package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FloorIlluminatedCabinetBlock extends FloorCabinetBlock {
    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<FloorIlluminatedCabinetBlock> CODEC = simpleCodec(FloorIlluminatedCabinetBlock::new);

    @Override
    protected @NotNull MapCodec<? extends FloorIlluminatedCabinetBlock> codec() {
        return CODEC;
    }
    #endif

    public static final Properties defaultSettings = IlluminatedCabinetBlock.defaultSettingsSupplier.get();

    public FloorIlluminatedCabinetBlock(Properties settings) { super(settings); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FloorIlluminatedCabinetBlockEntity(pos, state);
    }
}
