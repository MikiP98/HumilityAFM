package io.github.mikip98.humilityafm.content.blocks.cabinet;

#if MC_VERSION >= 12003 import com.mojang.serialization.MapCodec; #endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
#if POLYMER import io.github.mikip98.humilityafm.registries.Polymer; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FloorCabinetBlock extends CabinetBlock implements EntityBlock {
    #if MC_VERSION >= 12003
    protected static final MapCodec<FloorCabinetBlock> CODEC = simpleCodec(FloorCabinetBlock::new);

    @Override
    protected @NotNull MapCodec<? extends FloorCabinetBlock> codec() {
        return CODEC;
    }
    #endif

    protected static final VoxelShape voxelShapeOpenBottom = Block.box(1, 0, 1, 15, 3, 15);
    protected static final VoxelShape voxelShapeOpenTop = Block.box(1, 13, 1, 15, 16, 15);
    protected static final VoxelShape voxelShapeBottom = Shapes.or(voxelShapeOpenBottom, Block.box(1, 3.001, 1, 15, 4, 15));
    protected static final VoxelShape voxelShapeTop = Shapes.or(voxelShapeOpenTop, Block.box(1, 12, 1, 15, 12.999, 15));

    protected static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    public FloorCabinetBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(HALF, Half.BOTTOM));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        final boolean isBottomHalf = state.getValue(HALF) == Half.BOTTOM;
        if (state.getValue(OPEN)) return isBottomHalf ? voxelShapeOpenBottom : voxelShapeOpenTop;
        else return isBottomHalf ? voxelShapeBottom : voxelShapeTop;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(HALF, ctx.getClickedFace() == Direction.UP ? Half.BOTTOM : Half.TOP);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FloorCabinetBlockEntity(pos, state);
    }

    #if POLYMER
    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return state.getValue(HALF) == Half.BOTTOM ? Polymer.CABINET_BOTTOM_DISGUISE : Polymer.CABINET_TOP_DISGUISE;
    }
    #endif
}
