package io.github.mikip98.humilityafm.content.blocks.cabinet;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.util.SoundUtils;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import org.jetbrains.annotations.NotNull;

public class CabinetBlock extends HorizontalFacingBlock implements Waterloggable, BlockEntityProvider {
    protected static final VoxelShape voxelShapeOpenNorth = VoxelShapes.cuboid(0.0625f, 0.0625f, 0.81252f, 0.9375f, 0.9375f, 1.0f);  //open, reverse original
    protected static final VoxelShape voxelShapeOpenSouth = VoxelShapes.cuboid(0.0625f, 0.0625f, 0.0f, 0.9375f, 0.9375f, 0.18748f);  //open, original
    protected static final VoxelShape voxelShapeOpenEast = VoxelShapes.cuboid(0.0f, 0.0625f, 0.0625f, 0.18748f, 0.9375f, 0.9375f);  //open, swap z <-> x
    protected static final VoxelShape voxelShapeOpenWest = VoxelShapes.cuboid(0.81252f, 0.0625f, 0.0625f, 1.0f, 0.9375f, 0.9375f);  //open, reverse + swap

    protected static final VoxelShape voxelShapeClosedNorth = VoxelShapes.union(voxelShapeOpenNorth, VoxelShapes.cuboid(0.0625f, 0.0625f, 0.75f, 0.9375f, 0.9375f, 0.81248f));  //reverse original
    protected static final VoxelShape voxelShapeClosedSouth = VoxelShapes.union(voxelShapeOpenSouth, VoxelShapes.cuboid(0.0625f, 0.0625f, 0.18752f, 0.9375f, 0.9375f, 0.25f));  //original
    protected static final VoxelShape voxelShapeClosedEast = VoxelShapes.union(voxelShapeOpenEast, VoxelShapes.cuboid(0.18752f, 0.0625f, 0.0625f, 0.25f, 0.9375f, 0.9375f));  //swap z <-> x
    protected static final VoxelShape voxelShapeClosedWest = VoxelShapes.union(voxelShapeOpenWest, VoxelShapes.cuboid(0.75f, 0.0625f, 0.0625f, 0.81248f, 0.9375f, 0.9375f));  //reverse + swap

    protected static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final BooleanProperty OPEN = Properties.OPEN;
    protected static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    protected static final MapCodec<CabinetBlock> CODEC = createCodec(CabinetBlock::new);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }

    public static final FabricBlockSettings defaultSettings = FabricBlockSettings.create()
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
                .with(FACING, Direction.SOUTH)
                .with(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(OPEN)) {
            Inventory cabinetBlockEntity = (Inventory) world.getBlockEntity(pos);
            assert cabinetBlockEntity != null;

            ItemStack playerItemStack = player.getStackInHand(hand);
            if (!playerItemStack.isEmpty()) {
                if (playerItemStack.getItem() != cabinetBlockEntity.getStack(0).getItem()) {
                    // Take from the player the new ItemStack to insert into the Cabinet
                    ItemStack newCabinetItemStack = player.getStackInHand(hand).split(1);

                    // If the Cabinet already holds an item, give it to the player
                    if (!cabinetBlockEntity.getStack(0).isEmpty())
                        player.getInventory().offerOrDrop(cabinetBlockEntity.getStack(0));

                    // Put the new ItemStack inside the Cabinet
                    cabinetBlockEntity.setStack(0, newCabinetItemStack);
                }
                else return ActionResult.FAIL;
            } else {
                if (player.isSneaking()) {
                    // Give the player the stack from the Cabinet inventory
                    player.getInventory().offerOrDrop(cabinetBlockEntity.getStack(0));
                    // Remove the stack from Cabinet the inventory
                    cabinetBlockEntity.clear();
                }
                world.setBlockState(pos, state.with(OPEN, false));
            }
        } else world.setBlockState(pos, state.with(OPEN, true));

        playCabinetSound(world, pos);
        return ActionResult.SUCCESS;
    }
    protected void playCabinetSound(World world, BlockPos pos) {
        SoundUtils.playSoundAtBlockCenter(world, pos, SoundEvents.BLOCK_BAMBOO_BREAK, 0.35f);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(OPEN)) {
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
        return null;
    }

    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
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
                // Drop the item withing the Cabinet
                ItemStack stack = cabinetEntity.getItems().get(0);
                if (!stack.isEmpty()) Block.dropStack(world, pos, stack);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }
}
