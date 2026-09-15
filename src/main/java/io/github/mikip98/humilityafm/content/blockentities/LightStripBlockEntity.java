package io.github.mikip98.humilityafm.content.blockentities;

#if POLYMER import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.ElementHolder; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if POLYMER import io.github.mikip98.humilityafm.registries.Polymer; #endif
import net.minecraft.core.BlockPos;
#if POLYMER import net.minecraft.core.Direction; #endif
#if POLYMER import net.minecraft.server.level.ServerLevel; #endif
#if POLYMER import net.minecraft.util.Brightness; #endif
#if POLYMER import net.minecraft.world.item.ItemDisplayContext; #endif
#if POLYMER import net.minecraft.world.item.ItemStack; #endif
#if POLYMER import net.minecraft.world.item.Items; #endif
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.Half; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.StairsShape; #endif
#if POLYMER import net.minecraft.world.phys.Vec3; #endif
#if POLYMER import org.joml.Quaternionf; #endif
#if POLYMER import org.joml.Vector3f; #endif

public class LightStripBlockEntity extends BlockEntity {
    #if POLYMER protected final ItemDisplayElement display = new ItemDisplayElement(); #endif

    public LightStripBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, pos, state);

        #if POLYMER
        this.holder.addElement(display);
        display.setBrightness(new Brightness(15, 15));
        this.updateVisualState(state);
        #endif
    }

    #if POLYMER
    protected final ElementHolder holder = new ElementHolder();
    protected HolderAttachment attachment;

    public void updateVisualState(BlockState state) {
        final StairsShape shape = state.getValue(BlockStateProperties.STAIRS_SHAPE);

        PolymerModelData customData = null;
        if (shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT) {
            customData = Polymer.LIGHT_STRIP_INNER_MODELS.get(state.getBlock());
        } else if (shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT) {
            customData = Polymer.LIGHT_STRIP_OUTER_MODELS.get(state.getBlock());
        }

        if (customData != null) {
            ItemStack stack = new ItemStack(Items.GLOWSTONE_DUST);
            #if MC_VERSION < 12005
            stack.getOrCreateTag().putInt("CustomModelData", customData.value());
            #else
            stack.set(CUSTOM_MODEL_DATA, new CustomModelData(customData.value()));
            #endif
            display.setItem(stack);
        } else {
            display.setItem(state.getBlock().asItem().getDefaultInstance());
        }

        display.setModelTransformation(ItemDisplayContext.FIXED);

        final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int angleIndex = 0; // 0=South, 1=West, 2=North, 3=East
        switch (facing) {
            case SOUTH -> angleIndex = 0;
            case WEST -> angleIndex = 1;
            case NORTH -> angleIndex = 2;
            case EAST -> angleIndex = 3;
        }

        float xOffset = 0;
        float zOffset = 0;
        switch (angleIndex) {
            case 0 -> zOffset = 0.5f;       // South
            case 1 -> xOffset = -0.4375f;   // West
            case 2 -> zOffset = -0.5f;      // North
            case 3 -> xOffset = 0.4375f;       // East
        }

        if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
            if (facing == Direction.NORTH || facing == Direction.SOUTH) { // WORKS!
                switch (angleIndex) {
                    case 0 -> zOffset = 0.4375f;
                    case 2 -> zOffset = -0.4375f;
                }
                angleIndex = (angleIndex + 2) % 4;
            }
        }
        if (shape == StairsShape.INNER_RIGHT || shape == StairsShape.OUTER_RIGHT) {
            switch (angleIndex) {
                case 0 -> {  // South
                    xOffset = -0.4375f;
                    zOffset = 0;
                }
                case 1 -> {  // West
                    xOffset = 0;
                    zOffset = -0.4375f;
                }
                case 2 -> {  // North
                    xOffset = 0.4375f;
                    zOffset = 0;
                }
                case 3 -> {  // East
                    xOffset = 0;
                    zOffset = 0.4375f;
                }
            }
            if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                angleIndex = (angleIndex + 1) % 4;
            } else {
                angleIndex = (angleIndex + 3) % 4;
            }
        }

        float yRotation = (float) (angleIndex * (Math.PI / 2));

        float yOffset = state.getValue(BlockStateProperties.HALF) == Half.TOP ? 0.203125f : -0.734375f;

        display.setTranslation(new Vector3f(xOffset, yOffset, zOffset));
        display.setLeftRotation(new Quaternionf().rotateY(yRotation));
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        if (this.level instanceof ServerLevel serverLevel) {
            final Vec3 offsetPos = Vec3.atCenterOf(this.worldPosition);
            this.attachment = ChunkAttachment.ofTicking(this.holder, serverLevel, offsetPos);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.attachment != null) {
            this.attachment.destroy();
            this.attachment = null;
        }
    }
    #endif
}
