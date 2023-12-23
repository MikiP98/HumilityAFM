package io.github.mikip98.content.blocks.candlestick;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CandlestickWithCandle extends Candlestick{
    public static final BooleanProperty LIT = Properties.LIT;

    private boolean calculatedOffset = false;
    private double offsetX = 0.5;
    private final double offsetY = 0.85;
    private double offsetZ = 0.5;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    public CandlestickWithCandle(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(LIT, false)
                .with(Properties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL) {
//            switch (state.get(Properties.HORIZONTAL_FACING)) {
//                case NORTH:
//                    this.offsetZ = 0.35;
//                    break;
//                case SOUTH:
//                    this.offsetZ = 0.65;
//                    break;
//                case EAST:
//                    this.offsetX = 0.35;
//                    break;
//                case WEST:
//                    this.offsetX = 0.65;
//                    break;
//            }
            world.setBlockState(pos, state.with(LIT, !state.get(LIT)));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        // Check if the block is lit (property LIT is true)
        if (state.get(LIT)) {
            double x = pos.getX();// + this.offsetX;
            double y = pos.getY();// + this.offsetY;
            double z = pos.getZ();// + this.offsetZ;
            switch (state.get(Properties.HORIZONTAL_FACING)) {
                case NORTH:
                    z += 0.35;
                    break;
                case SOUTH:
                    z += 0.65;
                    break;
                case EAST:
                    x += 0.35;
                    break;
                case WEST:
                    x += 0.65;
                    break;
            }
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.01D, 0.0D);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(Properties.HORIZONTAL_FACING);
        VoxelShape shape;
        switch (direction) {
            case NORTH:
                shape = Block.createCuboidShape(7.5, 4, 10, 8.5, 5, 16);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7.5, 5, 10, 8.5, 8, 11), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(6, 7.999, 8.5, 10, 8.001, 12.5), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 9.5, 9, 11, 11.5), BooleanBiFunction.OR);  // Holder & Candle
                return shape;
            case SOUTH:
                shape = Block.createCuboidShape(7.5, 4, 0, 8.5, 5, 6);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7.5, 5, 5, 8.5, 8, 6), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(6, 7.999, 3.5, 10, 8.001, 7.5), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 4.5, 9, 11, 6.5), BooleanBiFunction.OR);  // Holder & Candle
                return shape;
//                return Block.createCuboidShape(5, 0, 0, 11, 16, 5);
            case EAST:
                shape = Block.createCuboidShape(0, 4, 7.5, 6, 5, 8.5);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(5, 5, 7.5, 6, 8, 8.5), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(3.5, 7.999, 6, 7.5, 8.001, 10), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(4.5, 8, 7, 6.5, 11, 9), BooleanBiFunction.OR);  // Holder & Candle
                return shape;
//                return Block.createCuboidShape(0, 0, 5, 5, 16, 11);
            case WEST:
                shape = Block.createCuboidShape(10, 4, 7.5, 16, 5, 8.5);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(10, 5, 7.5, 11, 8, 8.5), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(8.5, 7.999, 6, 12.5, 8.001, 10), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(9.5, 8, 7, 11.5, 11, 9), BooleanBiFunction.OR);  // Holder & Candle
                return shape;
            default:
                return Block.createCuboidShape(5, 0, 11, 11, 16, 16);
        }
    }
}
