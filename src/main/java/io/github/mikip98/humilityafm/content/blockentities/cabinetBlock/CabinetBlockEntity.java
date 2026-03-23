package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
#if MC_VERSION >= 12105
import net.minecraft.block.Block;
#endif
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
#if MC_VERSION >= 12006
import net.minecraft.registry.RegistryWrapper;
#endif
#if MC_VERSION >= 12108
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
#endif
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class CabinetBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public CabinetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CABINET_BLOCK_ENTITY, pos, state);
    }

    // Required by ImplementedInventory
    @Override
    public DefaultedList<ItemStack> getItems() {
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
    #else
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
    #endif
    #if MC_VERSION < 12006
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    #else
    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
    #endif
    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    #if MC_VERSION >= 12105
    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        final ItemStack stack = this.items.getFirst();
        if (!stack.isEmpty() && this.world != null) {
            Block.dropStack(this.world, pos, stack);
        }
        super.onBlockReplaced(pos, oldState);
    }
    #endif

    // Required to block hoppers from working
    @Override
    public int[] getAvailableSlots(Direction var1) { return new int[0]; }
    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) { return false; }
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) { return false; }
}
