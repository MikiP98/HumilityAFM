package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
#if MC_VERSION < 12105
import net.minecraft.core.HolderLookup;
#endif
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CabinetBlockEntity extends BlockEntity implements ImplementedInventory, WorldlyContainer {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public CabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CABINET_BLOCK_ENTITY, pos, state);
    }

    // Required by ImplementedInventory
    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }


    // Required for the stored item to show up
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
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
    }
    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
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
}
