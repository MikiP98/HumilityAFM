package io.github.mikip98.content.blocks.stairs;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class InnerStairs extends OuterStairs {
    public InnerStairs(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        VoxelShape base = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
        float y = 0.5f;
        if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
            base = VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f);
            y = 0f;
        }
        VoxelShape result;
        VoxelShape longShape;
        VoxelShape shortShape;
        switch(dir) {
            case NORTH:
                longShape = VoxelShapes.cuboid(0.5f, y, 0f, 1f, y+0.5f, 1f);
                shortShape = VoxelShapes.cuboid(0f, y, 0.5f, 0.5f, y+0.5f, 1f);
                result = VoxelShapes.combine(longShape, shortShape, BooleanBiFunction.OR); // original
                break;
            case SOUTH:
                longShape = VoxelShapes.cuboid(0f, y, 0f, 0.5f, y+0.5f, 1f);
                shortShape = VoxelShapes.cuboid(0.5f, y, 0f, 1f, y+0.5f, 0.5f);
                result = VoxelShapes.combine(longShape, shortShape, BooleanBiFunction.OR); //reverse original
                break;
            case EAST:
                longShape = VoxelShapes.cuboid(0f, y, 0.5f, 1f, y+0.5f, 1f);
                shortShape = VoxelShapes.cuboid(0f, y, 0f, 0.5f, y+0.5f, 0.5f);
                result = VoxelShapes.combine(longShape, shortShape, BooleanBiFunction.OR); //swap z <-> x + reverse
                break;
            case WEST:
                longShape = VoxelShapes.cuboid(0f, y, 0f, 1f, y+0.5f, 0.5f);
                shortShape = VoxelShapes.cuboid(0.5f, y, 0.5f, 1f, y+0.5f, 1f);
                result = VoxelShapes.combine(longShape, shortShape, BooleanBiFunction.OR); //swap
                break;
            default:
                return VoxelShapes.fullCube();
        }
        return VoxelShapes.combine(base, result, BooleanBiFunction.OR);
    }
}
