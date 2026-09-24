package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
#if POLYMER import net.minecraft.util.Brightness; #endif
import net.minecraft.world.level.block.state.BlockState;

public class FloorIlluminatedCabinetBlockEntity extends FloorCabinetBlockEntity {
    public FloorIlluminatedCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY, pos, state);
        #if POLYMER
        this.storedItemDisplay.setBrightness(new Brightness(15, 15));  // .getBrightness() returns null
        #endif
    }
}
