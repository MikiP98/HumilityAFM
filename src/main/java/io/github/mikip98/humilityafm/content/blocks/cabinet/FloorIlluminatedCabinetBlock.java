package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class FloorIlluminatedCabinetBlock extends FloorCabinetBlock {
    public static final FabricBlockSettings defaultSettings = FabricBlockSettings.copyOf(CabinetBlock.defaultSettings).luminance(2);

    public FloorIlluminatedCabinetBlock() { this(defaultSettings); }
    public FloorIlluminatedCabinetBlock(Item item) { this(defaultSettings, item); }
    public FloorIlluminatedCabinetBlock(Settings settings) { super(settings); }
    public FloorIlluminatedCabinetBlock(Settings settings, Item item) { super(settings, item); }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FloorIlluminatedCabinetBlockEntity(pos, state);
    }
}
