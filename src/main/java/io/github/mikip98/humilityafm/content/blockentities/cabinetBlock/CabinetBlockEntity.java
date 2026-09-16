package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

#if POLYMER import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
#if POLYMER import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerBlockEntities; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerModelCache; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.mikip98.humilityval.content.block.entity.AVLDataInput;
import io.mikip98.humilityval.content.block.entity.AVLDataOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.WorldlyContainer;
#if POLYMER import net.minecraft.world.item.ItemDisplayContext; #endif
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= 12105 import net.minecraft.world.level.storage.ValueInput; #endif
#if MC_VERSION >= 12105 import net.minecraft.world.level.storage.ValueOutput; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
#if POLYMER import org.joml.Quaternionf; #endif
#if POLYMER import org.joml.Vector3f; #endif

public class CabinetBlockEntity
        extends #if POLYMER PolymerBlockEntities.PolymerBlockEntityBase #else AVLBlockEntity #endif
        implements ImplementedInventory, WorldlyContainer {

    protected final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    #if POLYMER
    protected final ItemDisplayElement cabinetDisplay = display;
    protected final ItemDisplayElement storedItemDisplay = new ItemDisplayElement();
    #endif

    public CabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state #if POLYMER, false, true #endif);

        #if POLYMER
        cabinetDisplay.setScale(new Vector3f(1.0045f));

        storedItemDisplay.setModelTransformation(ItemDisplayContext.FIXED);
        storedItemDisplay.setScale(new Vector3f(0.59375f));

        cabinetDisplay.setLeftRotation(getCabinetRotation(state));
        storedItemDisplay.setLeftRotation(getBaseRotation(state));

        applyTranslations(state, cabinetDisplay, storedItemDisplay);

        holder.addElement(storedItemDisplay);

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

    @Override
    protected void avlSaveAdditional(AVLDataOutput out) {
        out.saveItems(this.items);
    }

    @Override
    protected void avlLoadAdditional(AVLDataInput in) {
        in.loadItems(this.items);
        #if POLYMER storedItemDisplay.setItem(this.items.get(0)); #endif
    }

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
        final float angle = getRotationNSSwap(state);
        return new Quaternionf().rotationY(angle);
    }
    protected void applyTranslations(BlockState state, ItemDisplayElement cabDisplay, ItemDisplayElement itemDisplay) {
        final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        final float pushBack = 0.4525f;
        final float itemPushBack = 0.41f;

        final Vector3f cabTrans = new Vector3f(facing.getStepX() * -pushBack, 0, facing.getStepZ() * -pushBack);
        final Vector3f itemTrans = new Vector3f(facing.getStepX() * -itemPushBack, 0, facing.getStepZ() * -itemPushBack);

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
        final ItemStack result = ImplementedInventory.super.removeItem(slot, count);
        if (slot == 0) this.storedItemDisplay.setItem(getItems().get(0));
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        final ItemStack result = ImplementedInventory.super.removeItemNoUpdate(slot);
        if (slot == 0) this.storedItemDisplay.setItem(ItemStack.EMPTY);
        return result;
    }

    @Override
    public void clearContent() {
        ImplementedInventory.super.clearContent();
        this.storedItemDisplay.setItem(ItemStack.EMPTY);
    }

    public void updateVisualState(boolean isOpen) {
        if (isOpen) {
            final PolymerModelData openData = PolymerModelCache.CABINET_OPEN_MODELS.get(this.getBlockState().getBlock());
            final ItemStack openStack = disguiseItem(openData);
            this.cabinetDisplay.setItem(openStack);
        } else {
            this.cabinetDisplay.setItem(this.getBlockState().getBlock().asItem().getDefaultInstance());
        }
    }
    #endif
}
