package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

#if POLYMER import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.Half; #endif
#if POLYMER import org.joml.Quaternionf; #endif
#if POLYMER import org.joml.Vector3f; #endif

public class FloorCabinetBlockEntity extends CabinetBlockEntity {
    public FloorCabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public FloorCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLOOR_CABINET_BLOCK_ENTITY, pos, state);
    }

    #if POLYMER
    protected Quaternionf getBaseRotation(BlockState state) {
        Quaternionf rotation = super.getBaseRotation(state);
        if (state.getValue(BlockStateProperties.HALF) == Half.BOTTOM) {
            rotation.rotateX((float) -Math.PI/2);
        } else {
            rotation.rotateX((float) Math.PI/2);
        }
        return rotation;
    }
    protected void applyTranslations(BlockState state, ItemDisplayElement cabDisplay, ItemDisplayElement itemDisplay) {
        Half half = state.getValue(BlockStateProperties.HALF);

        float pushBack = 0.4525f;
        float itemPushBack = 0.41f;

        Vector3f cabTrans = new Vector3f();
        Vector3f itemTrans = new Vector3f();

        if (half == Half.BOTTOM) {
            cabTrans.set(0, -pushBack, 0);
            itemTrans.set(0, -itemPushBack, 0);
        } else {
            cabTrans.set(0, pushBack, 0);
            itemTrans.set(0, itemPushBack, 0);
        }

        cabDisplay.setTranslation(cabTrans);
        itemDisplay.setTranslation(itemTrans);
    }
    #endif
}
