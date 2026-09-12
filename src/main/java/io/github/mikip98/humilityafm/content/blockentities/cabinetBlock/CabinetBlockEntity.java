package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

#if POLYMER import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.ElementHolder; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if POLYMER import io.github.mikip98.humilityafm.registries.Polymer; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
#if POLYMER import net.minecraft.world.item.ItemDisplayContext; #endif
import net.minecraft.world.item.ItemStack;
#if POLYMER import net.minecraft.world.item.Items; #endif
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= 12105
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
#endif
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
#if POLYMER import org.joml.Quaternionf; #endif
#if POLYMER import org.joml.Vector3f; #endif

public class CabinetBlockEntity extends BlockEntity implements ImplementedInventory, WorldlyContainer {
    protected final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    #if POLYMER
    protected final ElementHolder holder = new ElementHolder();
    protected HolderAttachment attachment;
    protected final ItemDisplayElement cabinetDisplay = new ItemDisplayElement();
    protected final ItemDisplayElement storedItemDisplay = new ItemDisplayElement();
    #endif

    public CabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        #if POLYMER
        cabinetDisplay.setItem(state.getBlock().asItem().getDefaultInstance());
        cabinetDisplay.setModelTransformation(ItemDisplayContext.FIXED);
        cabinetDisplay.setScale(new Vector3f(1.0045f));

        storedItemDisplay.setModelTransformation(ItemDisplayContext.FIXED);
        storedItemDisplay.setScale(new Vector3f(0.59375f));

        cabinetDisplay.setLeftRotation(getCabinetRotation(state));
        storedItemDisplay.setLeftRotation(getBaseRotation(state));

        applyTranslations(state, cabinetDisplay, storedItemDisplay);

        this.holder.addElement(cabinetDisplay);
        this.holder.addElement(storedItemDisplay);

        boolean isOpen = state.getValue(BlockStateProperties.OPEN);
        updateVisualState(isOpen);
        #endif
    }
    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityRegistry.CABINET_BLOCK_ENTITY, pos, state);
    }

    // Required by ImplementedInventory
    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }


    // TODO: Move to AVLBlockEntity
    #if MC_VERSION < 12006
    // 1.20.0 to 1.20.4
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, this.items);
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    #elif MC_VERSION < 12105
    // 1.20.6 to 1.21.4
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    #else
    // 1.21.5+
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
    }
    @Override
    protected void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.items, false);
        super.saveAdditional(output);
    }
    #endif

    @Override
    #if MC_VERSION < 12006
    // 1.20.1 - 1.20.4
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
    #else
    // 1.20.6+
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
    #endif

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Required to block hoppers from working
    @Override
    public int @NotNull [] getSlotsForFace(Direction side) { return new int[0]; }
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) { return false; }
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return false; }


    #if POLYMER
    protected Quaternionf getCabinetRotation(BlockState state) {
        Quaternionf rot = getBaseRotation(state);
        rot.rotateZ((float) Math.PI);
        return rot;
    }
    protected Quaternionf getBaseRotation(BlockState state) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float angle = switch (facing) {
            case NORTH -> (float) Math.PI;
            case SOUTH -> 0f;
            case EAST -> (float) (Math.PI * 0.5);
            case WEST -> (float) (Math.PI * 1.5);
            default -> throw new IllegalStateException();
        };
        return new Quaternionf().rotationY(angle);
    }
    protected void applyTranslations(BlockState state, ItemDisplayElement cabDisplay, ItemDisplayElement itemDisplay) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        float pushBack = 0.4525f;
        float itemPushBack = 0.41f;

        Vector3f cabTrans = new Vector3f();
        Vector3f itemTrans = new Vector3f();

        switch (facing) {
            case NORTH -> { cabTrans.set(0, 0, pushBack); itemTrans.set(0, 0, itemPushBack); }
            case SOUTH -> { cabTrans.set(0, 0, -pushBack); itemTrans.set(0, 0, -itemPushBack); }
            case EAST -> { cabTrans.set(-pushBack, 0, 0); itemTrans.set(-itemPushBack, 0, 0); }
            case WEST -> { cabTrans.set(pushBack, 0, 0); itemTrans.set(itemPushBack, 0, 0); }
        }

        cabDisplay.setTranslation(cabTrans);
        itemDisplay.setTranslation(itemTrans);
    }

    //TODO: Check if the 4 overrides below are necessary

    @Override
    public void setItem(int slot, ItemStack stack) {
        ImplementedInventory.super.setItem(slot, stack);
        if (slot == 0) this.storedItemDisplay.setItem(stack);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        ItemStack result = ImplementedInventory.super.removeItem(slot, count);
        if (slot == 0) this.storedItemDisplay.setItem(getItems().get(0));
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ImplementedInventory.super.removeItemNoUpdate(slot);
        if (slot == 0) this.storedItemDisplay.setItem(ItemStack.EMPTY);
        return result;
    }

    @Override
    public void clearContent() {
        ImplementedInventory.super.clearContent();
        this.storedItemDisplay.setItem(ItemStack.EMPTY);
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

    public void updateVisualState(boolean isOpen) {
        if (isOpen) {
            final PolymerModelData openData = Polymer.CABINET_OPEN_MODELS.get(this.getBlockState().getBlock());
            if (openData != null) {
                ItemStack openStack = new ItemStack(Items.GLOWSTONE_DUST);

                #if MC_VERSION < 12006
                openStack.getOrCreateTag().putInt("CustomModelData", openData.value());
                #else
                openStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(openData.value()));
                #endif

                this.cabinetDisplay.setItem(openStack);
            }
        } else {
            this.cabinetDisplay.setItem(this.getBlockState().getBlock().asItem().getDefaultInstance());
        }
    }
    #endif
}
