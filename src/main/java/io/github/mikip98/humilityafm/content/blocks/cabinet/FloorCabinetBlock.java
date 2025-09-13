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
    protected static final VoxelShape voxelShapeOpenBottom = Block.createCuboidShape(1, 0, 1, 15, 3, 15);
    protected static final VoxelShape voxelShapeOpenTop = Block.createCuboidShape(1, 13, 1, 15, 16, 15);
    protected static final VoxelShape voxelShapeBottom = VoxelShapes.union(voxelShapeOpenBottom, Block.createCuboidShape(1, 3.001, 1, 15, 4, 15));
    protected static final VoxelShape voxelShapeTop = VoxelShapes.union(voxelShapeOpenTop, Block.createCuboidShape(1, 12, 1, 15, 12.999, 15));

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
        final boolean isBottom = state.get(HALF) == BlockHalf.BOTTOM;
        if (state.get(OPEN)) return isBottom ? voxelShapeOpenBottom : voxelShapeOpenTop;
        else return isBottom ? voxelShapeBottom : voxelShapeTop;
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
