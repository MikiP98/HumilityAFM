package io.github.mikip98.humilityafm.content.blocks.cabinet;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
#if MC_VERSION < 260000
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
#else
import io.github.mikip98.humilityafm.util.wrappers.BlockHalfWrapper;
import io.github.mikip98.humilityafm.util.wrappers.BlockStateWrapper;
import io.github.mikip98.humilityafm.util.wrappers.PropertiesWrapper;
import io.github.mikip98.humilityafm.util.wrappers.VoxelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
#endif
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class FloorCabinetBlock extends CabinetBlock implements
        #if MC_VERSION < 260000 Waterloggable, BlockEntityProvider #else SimpleWaterloggedBlock, EntityBlock #endif
{
    protected static final VoxelShape voxelShapeOpenBottom = VoxelUtils.cuboid(1, 0, 1, 15, 3, 15);
    protected static final VoxelShape voxelShapeOpenTop = VoxelUtils.cuboid(1, 13, 1, 15, 16, 15);
    protected static final VoxelShape voxelShapeBottom = VoxelUtils.union(voxelShapeOpenBottom, VoxelUtils.cuboid(1, 3.001, 1, 15, 4, 15));
    protected static final VoxelShape voxelShapeTop = VoxelUtils.union(voxelShapeOpenTop, VoxelUtils.cuboid(1, 12, 1, 15, 12.999, 15));

    protected static final EnumProperty<#if MC_VERSION < 260000 BlockHalf #else Half> HALF = PropertiesWrapper.HALF;

    @Override
    #if MC_VERSION < 260000
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    #else
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    #endif
        builder.add(HALF);
    }

    public FloorCabinetBlock(#if MC_VERSION < 260000 Settings #else Properties #endif settings) {
        super(settings);
        updateDefaultState(Map.of(HALF, BlockHalfWrapper.BOTTOM));
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
        final boolean isBottom = BlockStateWrapper.get(state, HALF) == BlockHalfWrapper.BOTTOM;
        if (BlockStateWrapper.get(state, OPEN)) return isBottom ? voxelShapeOpenBottom : voxelShapeOpenTop;
        else return isBottom ? voxelShapeBottom : voxelShapeTop;
    }

    @Override
    #if MC_VERSION < 260000
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                .with(HALF, ctx.getSide() == Direction.UP ? BlockHalfWrapper.BOTTOM : BlockHalfWrapper.TOP);
    }
    #else
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(HALF, ctx.getClickedFace() == Direction.UP ? BlockHalfWrapper.BOTTOM : BlockHalfWrapper.TOP);
    }
    #endif

    @Override
    #if MC_VERSION < 260000
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    #else
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    #endif
        return new FloorCabinetBlockEntity(pos, state);
    }
}
