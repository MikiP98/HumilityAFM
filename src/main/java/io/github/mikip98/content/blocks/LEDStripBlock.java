package io.github.mikip98.content.blocks;

import io.github.mikip98.content.blockentities.LEDBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class LEDStripBlock extends StairsBlock implements BlockEntityProvider {
    public LEDStripBlock(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
            switch(dir) {
                case NORTH:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0f, 1f, 1f, 0.0625f);
                case SOUTH:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 1f, 1f, 1f);
                case EAST:
                    return VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 1f);
                case WEST:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 1f);
                default:
                    return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
            }
        }
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.0625f, 0.0625f);
            case SOUTH:
                return VoxelShapes.cuboid(0f, 0f, 0.9375f, 1f, 0.0625f, 1f);
            case EAST:
                return VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 0.0625f, 1f);
            case WEST:
                return VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 1f);
            default:
                return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LEDBlockEntity(pos, state);
    }
}
