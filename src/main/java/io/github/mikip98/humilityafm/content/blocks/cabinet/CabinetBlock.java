package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION >= 12004 import com.mojang.serialization.MapCodec; #endif
#if POLYMER import eu.pb4.polymer.core.api.block.PolymerBlock; #endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.ImplementedInventory;
import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
#if POLYMER import net.minecraft.world.level.block.Blocks; #endif
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CabinetBlock extends PlainHorizontalFacingBlock implements Waterloggable, EntityBlock #if POLYMER, PolymerBlock #endif {
    protected static final VoxelShape voxelShapeOpenNorth = Block.box(1, 1, 13.00032, 15, 15, 16);  //open, reverse original
    protected static final VoxelShape voxelShapeOpenSouth = Block.box(1, 1, 0, 15, 15, 2.99968);  //open, original
    protected static final VoxelShape voxelShapeOpenEast = Block.box(0, 1, 1, 2.99968, 15, 15);  //open, swap z <-> x
    protected static final VoxelShape voxelShapeOpenWest = Block.box(13.00032, 1, 1, 16, 15, 15);  //open, reverse + swap

    protected static final VoxelShape voxelShapeClosedNorth = Shapes.or(voxelShapeOpenNorth, Block.box(1, 1, 12, 15, 15, 12.99968));  //reverse original
    protected static final VoxelShape voxelShapeClosedSouth = Shapes.or(voxelShapeOpenSouth, Block.box(1, 1, 3.00032, 15, 15, 4));  //original
    protected static final VoxelShape voxelShapeClosedEast = Shapes.or(voxelShapeOpenEast, Block.box(3.00032, 1, 1, 4, 15, 15));  //swap z <-> x
    protected static final VoxelShape voxelShapeClosedWest = Shapes.or(voxelShapeOpenWest, Block.box(12, 1, 1, 12.99968, 15, 15));  //reverse + swap

    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    #if MC_VERSION >= 12004
    protected static final MapCodec<CabinetBlock> CODEC = simpleCodec(CabinetBlock::new);

    @Override
    protected @NotNull MapCodec<? extends CabinetBlock> codec() { return CODEC; }
    #endif

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
        builder.add(WATERLOGGED);
    }

    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.of()
            .strength(2.0f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.WOOD);

    public static final Properties defaultSettings = defaultSettingsSupplier.get();


    public CabinetBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(OPEN, false)
                .setValue(WATERLOGGED, false));
    }


    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        return onUseLogic(state, level, pos, player, hand);
    }
    #else
    public @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull BlockHitResult hit
    ) {
        final InteractionHand hand = player.getUsedItemHand();
        return onUseLogic(state, level, pos, player, hand);
    }
    #endif

    protected static InteractionResult onUseLogic(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (state.getValue(OPEN)) {
            final ImplementedInventory cabinetBlockEntity = (ImplementedInventory) world.getBlockEntity(pos);
            assert cabinetBlockEntity != null;

            final ItemStack playerItemStack = player.getItemInHand(hand);
            final ItemStack oldCabinetItemStack = cabinetBlockEntity.getItem(0);

            final boolean playerHoldsItem = !playerItemStack.isEmpty();
            if (playerHoldsItem) {
                if (playerItemStack.getItem() != oldCabinetItemStack.getItem()) {
                    ItemStack newCabinetItemStack = playerItemStack.split(1);

                    if (!oldCabinetItemStack.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(oldCabinetItemStack);
                    }

                    cabinetBlockEntity.setItem(0, newCabinetItemStack);
                }
                else return InteractionResult.FAIL;
            } else {
                if (player.isShiftKeyDown()) {
                    player.getInventory().placeItemBackInInventory(oldCabinetItemStack);
                    cabinetBlockEntity.clearContent();
                }
                world.setBlockAndUpdate(pos, state.setValue(OPEN, false));
            }
        } else world.setBlockAndUpdate(pos, state.setValue(OPEN, true));

        playCabinetSound(world, pos, player);
        return InteractionResult.SUCCESS;
    }
    protected static void playCabinetSound(Level world, BlockPos pos, Player player) {
        SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.BAMBOO_BREAK);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        final Direction dir = state.getValue(FACING);
        final boolean isOpen = state.getValue(OPEN);

        if (isOpen) {
            switch (dir) {
                case NORTH: return voxelShapeOpenNorth;
                case SOUTH: return voxelShapeOpenSouth;
                case EAST: return voxelShapeOpenEast;
                case WEST: return voxelShapeOpenWest;
            }
        } else {
            switch (dir) {
                case NORTH: return voxelShapeClosedNorth;
                case SOUTH: return voxelShapeClosedSouth;
                case EAST: return voxelShapeClosedEast;
                case WEST: return voxelShapeClosedWest;
            }
        }
        throw new IllegalStateException("It's not possible to get here...");
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, isPlacedInWater(ctx));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    #if MC_VERSION < 12105
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CabinetBlockEntity cabinetEntity) {
                // Drop the item within the Cabinet
                ItemStack stack = cabinetEntity.getItem(0);
                if (!stack.isEmpty()) {
                    Block.popResource(level, pos, stack);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    #endif

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }

    #if POLYMER
    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.BARRIER;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.BARRIER.defaultBlockState();
    }
    #endif
}
