package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.ImplementedInventory;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.util.wrappers.*;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.Supplier;

public class CabinetBlock extends PlainHorizontalFacingBlock implements SimpleWaterloggedBlock, EntityBlock {
    protected static final VoxelShape voxelShapeOpenNorth = Block.box(1, 1, 13.00032, 15, 15, 16);  //open, reverse original
    protected static final VoxelShape voxelShapeOpenSouth = Block.box(1, 1, 0, 15, 15, 2.99968);  //open, original
    protected static final VoxelShape voxelShapeOpenEast = Block.box(0, 1, 1, 2.99968, 15, 15);  //open, swap z <-> x
    protected static final VoxelShape voxelShapeOpenWest = Block.box(13.00032, 1, 1, 1.0f, 15, 15);  //open, reverse + swap

    protected static final VoxelShape voxelShapeClosedNorth = Shapes.or(voxelShapeOpenNorth, Block.box(1, 1, 12, 15, 15, 12.99968));  //reverse original
    protected static final VoxelShape voxelShapeClosedSouth = Shapes.or(voxelShapeOpenSouth, Block.box(1, 1, 3.00032, 15, 15, 4));  //original
    protected static final VoxelShape voxelShapeClosedEast = Shapes.or(voxelShapeOpenEast, Block.box(3.00032, 1, 1, 4, 15, 15));  //swap z <-> x
    protected static final VoxelShape voxelShapeClosedWest = Shapes.or(voxelShapeOpenWest, Block.box(12, 1, 1, 12.99968, 15, 15));  //reverse + swap

    protected static final BooleanProperty WATERLOGGED = PropertiesWrapper.WATERLOGGED;
    protected static final BooleanProperty OPEN = PropertiesWrapper.OPEN;

    #if MC_VERSION >= 12004
    protected static final MapCodec<CabinetBlock> CODEC = createCodec(CabinetBlock::new);

    @Override
    protected @NonNull MapCodec<? extends CabinetBlock> #if MC_VERSION < 260000 getCodec() #else codec() #endif { return CODEC; }
    #endif

    @Override
    #if MC_VERSION < 260000
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    #else
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    #endif
        builder.add(OPEN);
        builder.add(WATERLOGGED);
    }

    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.of()
            .strength(2.0f)
            #if MC_VERSION < 260000
            .requiresTool()
            .nonOpaque()
            .sounds(BlockSoundGroup.WOOD)
            #else
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.WOOD)
            #endif;

    public static final #if MC_VERSION < 260000 Settings #else Properties #endif defaultSettings = defaultSettingsSupplier.get();


    public CabinetBlock(#if MC_VERSION < 260000 Settings #else Properties #endif settings) {
        super(settings);
        updateDefaultState(Map.of(OPEN, false, WATERLOGGED, false));
    }


    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    #elif MC_VERSION < 260000
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        final Hand hand = player.getActiveHand();
    #else
    public @NonNull InteractionResult useWithoutItem(
            @NonNull BlockState state, @NonNull Level world, @NonNull BlockPos pos, Player player, @NonNull BlockHitResult hit
    ) {
        final InteractionHand hand = player.getUsedItemHand();
    #endif
        if (BlockStateWrapper.get(state, OPEN)) {
            ImplementedInventory cabinetBlockEntity = (ImplementedInventory) world.getBlockEntity(pos);
            assert cabinetBlockEntity != null;

            ItemStack playerItemStack = PlayerEntityWrapper.getStackInHand(player, hand);
            ItemStack cabinetItemStack = cabinetBlockEntity.getStack(0);
            if (!playerItemStack.isEmpty()) {
                if (playerItemStack.getItem() != cabinetItemStack.getItem()) {
                    // Take from the player the new ItemStack to insert into the Cabinet
                    ItemStack newCabinetItemStack = playerItemStack.split(1);

                    // If the Cabinet already holds an item, give it to the player
                    if (!cabinetBlockEntity.getStack(0).isEmpty())
                        PlayerWrapper.offerOrDrop(player, cabinetItemStack);

                    // Put the new ItemStack inside the Cabinet
                    cabinetBlockEntity.setStack(0, newCabinetItemStack);
                }
                else return ActionResultWrapper.FAIL;
            } else {
                if (PlayerWrapper.isSneaking(player)) {
                    // Give the player the stack from the Cabinet inventory
                    PlayerWrapper.offerOrDrop(player, cabinetItemStack);
                    // Remove the stack from Cabinet the inventory
                    cabinetBlockEntity.clear();
                }
                WorldWrapper.setBlockState(world, pos, BlockStateWrapper.with(state, OPEN, false));
            }
        } else WorldWrapper.setBlockState(world, pos, BlockStateWrapper.with(state, OPEN, true));

        playCabinetSound(world, pos, player);
        return ActionResultWrapper.SUCCESS;
    }
    protected static void playCabinetSound(
            #if MC_VERSION < 260000 World #else Level #endif world,
            BlockPos pos,
            #if MC_VERSION < 260000 PlayerEntity #else Player #endif player
    ) {
        SoundUtils.playSoundAtBlockCenter(
                world, player, pos,
                SoundEvents.#if MC_VERSION < 260000 BLOCK_BAMBOO_BREAK #else BAMBOO_BREAK #endif);
    }

    @Override
    #if MC_VERSION < 260000
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
    #else
    public @NonNull VoxelShape getShape(
            BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context
    ) {
    #endif
        final Direction dir = BlockStateWrapper.get(state, FACING);
        final boolean isOpen = BlockStateWrapper.get(state, OPEN);
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
    #if MC_VERSION < 260000
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }
    #else
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }
    #endif

    @Override
    #if MC_VERSION < 260000
    @SuppressWarnings("deprecation")
    public @NonNull FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }
    #else
    public @NonNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }
    #endif

    #if MC_VERSION < 12105
    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CabinetBlockEntity cabinetEntity) {
                // Drop the item withing the Cabinet
                #if MC_VERSION < 12006
                ItemStack stack = cabinetEntity.getItems().get(0);
                #else
                ItemStack stack = cabinetEntity.getItems().getFirst();
                #endif
                if (!stack.isEmpty()) Block.dropStack(world, pos, stack);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    #endif


    @Override
    #if MC_VERSION < 260000
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    #else
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    #endif
        return new CabinetBlockEntity(pos, state);
    }
}
