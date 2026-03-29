package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION < 260000
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
#endif
#if MC_VERSION >= 12006 && MC_VERSION < 260000
import net.minecraft.registry.RegistryWrapper;
#endif
#if MC_VERSION >= 12108 && MC_VERSION < 260000
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
#endif
#if MC_VERSION < 260000
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
#else
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
#endif

import org.jetbrains.annotations.Nullable;
#if MC_VERSION >= 260000
import org.jspecify.annotations.NonNull;
#endif

public class CabinetBlockEntity extends BlockEntity implements ImplementedInventory, #if MC_VERSION < 260000 SidedInventory #else WorldlyContainer #endif {
    #if MC_VERSION < 260000
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    #else
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    #endif

    public CabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CABINET_BLOCK_ENTITY, pos, state);
    }

    // Required by ImplementedInventory
    @Override
    public #if MC_VERSION < 260000 DefaultedList #else NonNullList #endif <ItemStack> getItems() {
        return this.items;
    }


    // Required for the stored item to show up
    #if MC_VERSION < 12006
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }
    #elif MC_VERSION < 12108
    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
    }
    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, items, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }
    #elif MC_VERSION < 260000
    @Override
    protected void readData(ReadView view) {
        super.readData(view);
//        this.items.clear();  // TODO: Make sure this is indeed required to prevent ghost items on clients
        Inventories.readData(view, this.items);
    }
    @Override
    protected void writeData(WriteView view) {
        Inventories.writeData(view, this.items, false);  // Make sure 'False' does not create ghost blocks on clients
        super.writeData(view);
    }
    #else
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
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    #elif MC_VERSION < 260000
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
    #else
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        return saveCustomOnly(registries);
    }
    #endif

    @Override
    #if MC_VERSION < 260000
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    #else
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    #endif

    // Required to block hoppers from working
    #if MC_VERSION < 260000
    @Override
    public int[] getAvailableSlots(Direction var1) { return new int[0]; }
    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) { return false; }
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) { return false; }
    #else
    @Override
    public int @NonNull [] getSlotsForFace(@NonNull Direction side) { return new int[0]; }
    @Override
    public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack stack, @Nullable Direction side) { return false; }
    @Override
    public boolean canTakeItemThroughFace(int slot, @NonNull ItemStack stack, @NonNull Direction side) { return false; }
    #endif
}
