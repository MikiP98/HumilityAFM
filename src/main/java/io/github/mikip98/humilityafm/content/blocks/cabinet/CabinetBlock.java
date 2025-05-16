package io.github.mikip98.humilityafm.content.blocks.cabinet;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;

public class CabinetBlock extends HorizontalFacingBlock implements Waterloggable, BlockEntityProvider {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty OPEN = Properties.OPEN;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(WATERLOGGED);
    }

    public static final FabricBlockSettings defaultSettings = FabricBlockSettings
            .create()
            .strength(2.0f)
            .requiresTool()
            .nonOpaque()
            .sounds(BlockSoundGroup.WOOD);


    public CabinetBlock() {
        this(defaultSettings);
    }
    public CabinetBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(OPEN, false)
                .with(Properties.HORIZONTAL_FACING, Direction.SOUTH)
                .with(Properties.WATERLOGGED, false));
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_BAMBOO_BREAK, SoundCategory.BLOCKS, 1, 1, true);

        if (world.getBlockState(pos).get(OPEN)){
            if (world.isClient) return ActionResult.SUCCESS;
            Inventory cabinetBlockEntity = (Inventory) world.getBlockEntity(pos);

            if (!player.getStackInHand(hand).isEmpty()) {

                assert cabinetBlockEntity != null;
                if (!cabinetBlockEntity.getStack(0).isEmpty()) {
                    // Give the player the stack in the inventory
                    player.getInventory().offerOrDrop(cabinetBlockEntity.getStack(0));
                    // Remove the stack from the inventory
                    cabinetBlockEntity.removeStack(0);
                }

                ItemStack stack = player.getStackInHand(hand).copy();

                // Remove the stack from the player's hand
                player.getStackInHand(hand).setCount(stack.getCount() - 1);

                stack.setCount(1);

                // Put one item from the stack the player is holding into the inventory
                cabinetBlockEntity.setStack(0, stack.copy());

                //Update block state?
                world.setBlockState(pos, state.with(OPEN, false));
                world.setBlockState(pos, state.with(OPEN, true));

            } else {
                if (player.isSneaking()) {
                    // Give the player the stack in the inventory
                    assert cabinetBlockEntity != null;
                    player.getInventory().offerOrDrop(cabinetBlockEntity.getStack(0));
                    // Remove the stack from the inventory
                    cabinetBlockEntity.removeStack(0);

                    //Update block state?
                    world.setBlockState(pos, state.with(OPEN, false));
                    world.setBlockState(pos, state.with(OPEN, true));
                }

                world.setBlockState(pos, state.with(OPEN, false));
            }
        } else {
            world.setBlockState(pos, state.with(OPEN, true));
        }

        return ActionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        boolean open = state.get(OPEN);

        if (open) {

            switch(dir) {
                case NORTH:
                    return VoxelShapes.cuboid(0.0625f, 0.0625f, 0.81252f, 0.9375f, 0.9375f, 1.0f); //open, reverse original, second half finished, to do
                case SOUTH:
                    return VoxelShapes.cuboid(0.0625f, 0.0625f, 0.0f, 0.9375f, 0.9375f, 0.18748f); //open, original
                case EAST:
                    return VoxelShapes.cuboid(0.0f, 0.0625f, 0.0625f, 0.18748f, 0.9375f, 0.9375f); //open, swap z <-> x
                case WEST:
                    return VoxelShapes.cuboid(0.81252f, 0.0625f, 0.0625f, 1.0f, 0.9375f, 0.9375f); //open, reverse + swap
                default:
                    return VoxelShapes.fullCube();
            }

        } else {

            switch(dir) {
                case NORTH:
                    return VoxelShapes.combine(VoxelShapes.cuboid(0.0625f, 0.0625f, 0.81252f, 0.9375f, 0.9375f, 1.0f), VoxelShapes.cuboid(0.0625f, 0.0625f, 0.75f, 0.9375f, 0.9375f, 0.81248f), BooleanBiFunction.OR); //reverse original, second half finished, to do
                case SOUTH:
                    return VoxelShapes.combine(VoxelShapes.cuboid(0.0625f, 0.0625f, 0.0f, 0.9375f, 0.9375f, 0.18748f), VoxelShapes.cuboid(0.0625f, 0.0625f, 0.18752f, 0.9375f, 0.9375f, 0.25f), BooleanBiFunction.OR); //original
                case EAST:
                    return VoxelShapes.combine(VoxelShapes.cuboid(0.0f, 0.0625f, 0.0625f, 0.18748f, 0.9375f, 0.9375f), VoxelShapes.cuboid(0.18752f, 0.0625f, 0.0625f, 0.25f, 0.9375f, 0.9375f), BooleanBiFunction.OR); //swap z <-> x
                case WEST:
                    return VoxelShapes.combine(VoxelShapes.cuboid(0.81252f, 0.0625f, 0.0625f, 1.0f, 0.9375f, 0.9375f), VoxelShapes.cuboid(0.75f, 0.0625f, 0.0625f, 0.81248f, 0.9375f, 0.9375f), BooleanBiFunction.OR); //reverse + swap
                default:
                    return VoxelShapes.fullCube();
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CabinetBlockEntity cabinetEntity) {
                // Handle item drops and block entity cleanup here
                DefaultedList<ItemStack> inventory = cabinetEntity.getItems();
                for (ItemStack stack : inventory) {
                    if (!stack.isEmpty()) {
                        Block.dropStack(world, pos, stack);
                    }
                }
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }
}
