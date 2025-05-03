package io.github.mikip98.humilityafm.content.blocks.cabinet;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;

public class IlluminatedCabinetBlock extends CabinetBlock {

    public static final FabricBlockSettings defaultSettings = FabricBlockSettings
            .copyOf(CabinetBlock.defaultSettings)
            .luminance(2);

    public IlluminatedCabinetBlock() { super(defaultSettings); }
    public IlluminatedCabinetBlock(Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedCabinetBlockEntity(pos, state);
    }
}
