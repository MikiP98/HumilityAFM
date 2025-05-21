package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class FloorCabinetBlock extends CabinetBlock implements Waterloggable, BlockEntityProvider {
    protected static final VoxelShape voxelShapeOpenNorth = VoxelShapes.cuboid(0.0625f, 0.0625f, 0.81252f, 0.9375f, 0.9375f, 1.0f);  //open, reverse original
    protected static final VoxelShape voxelShapeOpenSouth = VoxelShapes.cuboid(0.0625f, 0.0625f, 0.0f, 0.9375f, 0.9375f, 0.18748f);  //open, original
    protected static final VoxelShape voxelShapeOpenEast = VoxelShapes.cuboid(0.0f, 0.0625f, 0.0625f, 0.18748f, 0.9375f, 0.9375f);  //open, swap z <-> x
    protected static final VoxelShape voxelShapeOpenWest = VoxelShapes.cuboid(0.81252f, 0.0625f, 0.0625f, 1.0f, 0.9375f, 0.9375f);  //open, reverse + swap

    protected static final VoxelShape voxelShapeClosedNorth = VoxelShapes.union(voxelShapeOpenNorth, VoxelShapes.cuboid(0.0625f, 0.0625f, 0.75f, 0.9375f, 0.9375f, 0.81248f));  //reverse original
    protected static final VoxelShape voxelShapeClosedSouth = VoxelShapes.union(voxelShapeOpenSouth, VoxelShapes.cuboid(0.0625f, 0.0625f, 0.18752f, 0.9375f, 0.9375f, 0.25f));  //original
    protected static final VoxelShape voxelShapeClosedEast = VoxelShapes.union(voxelShapeOpenEast, VoxelShapes.cuboid(0.18752f, 0.0625f, 0.0625f, 0.25f, 0.9375f, 0.9375f));  //swap z <-> x
    protected static final VoxelShape voxelShapeClosedWest = VoxelShapes.union(voxelShapeOpenWest, VoxelShapes.cuboid(0.75f, 0.0625f, 0.0625f, 0.81248f, 0.9375f, 0.9375f));  //reverse + swap

    protected static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }


    public FloorCabinetBlock() {
        this(defaultSettings);
    }
    public FloorCabinetBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(HALF, BlockHalf.BOTTOM)
        );
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(OPEN)) {
            switch (dir) {
                case NORTH:
                    return voxelShapeOpenNorth;
                case SOUTH:
                    return voxelShapeOpenSouth;
                case EAST:
                    return voxelShapeOpenEast;
                case WEST:
                    return voxelShapeOpenWest;
            }
        } else {
            switch (dir) {
                case NORTH:
                    return voxelShapeClosedNorth;
                case SOUTH:
                    return voxelShapeClosedSouth;
                case EAST:
                    return voxelShapeClosedEast;
                case WEST:
                    return voxelShapeClosedWest;
            }
        }
        return null;
    }

    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                .with(HALF, ctx.getSide() == Direction.UP ? BlockHalf.BOTTOM : BlockHalf.TOP);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FloorCabinetBlockEntity(pos, state);
    }
}
