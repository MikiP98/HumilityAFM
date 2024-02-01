package io.github.mikip98.content.blocks.candlestick;

import io.github.mikip98.helpers.CandlestickHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static io.github.mikip98.HumilityAFM.LOGGER;

public class Candlestick extends HorizontalFacingBlock {
//    private static final String[] candlesCache = {"candle", "white_candle", "orange_candle", "magenta_candle", "light_blue_candle", "yellow_candle", "lime_candle", "pink_candle", "gray_candle", "light_gray_candle", "cyan_candle", "purple_candle", "blue_candle", "brown_candle", "green_candle", "red_candle", "black_candle"};

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    public Candlestick(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        LOGGER.info(heldItem.getItem().getTranslationKey());

        final String[] parts = heldItem.getItem().getTranslationKey().split("\\.");
        if (parts[2].endsWith("candle") && parts[1].equals("minecraft")) {
            // Replace the candlestick block with the version with the candle
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + parts[2]).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);
            world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CANDLE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);

            // Decrement candle amount in player's inventory
            if (!player.isCreative()) {
                heldItem.decrement(1);
            }

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(Properties.HORIZONTAL_FACING);
        VoxelShape shape;
        switch (direction) {
            case NORTH:
                shape = Block.createCuboidShape(7.5, 4, 10, 8.5, 5, 16);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7.5, 5, 10, 8.5, 8, 11), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(6, 7.999, 8.5, 10, 8.001, 12.5), BooleanBiFunction.OR); // Dripper
//                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 9.49, 9, 9, 9.51), BooleanBiFunction.OR);  // Front holder
//                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 11.49, 9, 9, 11.51), BooleanBiFunction.OR);  // Back holder
//                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7.99, 8, 9.5, 9.01, 9, 11.5), BooleanBiFunction.OR);  // Left holder
//                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(6.99, 8, 9.5, 7.01, 9, 11.5), BooleanBiFunction.OR);  // Right holder
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 9.5, 9, 9, 11.5), BooleanBiFunction.OR);  // Holder
                return shape;
            case SOUTH:
                shape = Block.createCuboidShape(7.5, 4, 0, 8.5, 5, 6);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7.5, 5, 5, 8.5, 8, 6), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(6, 7.999, 3.5, 10, 8.001, 7.5), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(7, 8, 4.5, 9, 9, 6.5), BooleanBiFunction.OR);  // Holder
                return shape;
//                return Block.createCuboidShape(5, 0, 0, 11, 16, 5);
            case EAST:
                shape = Block.createCuboidShape(0, 4, 7.5, 6, 5, 8.5);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(5, 5, 7.5, 6, 8, 8.5), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(3.5, 7.999, 6, 7.5, 8.001, 10), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(4.5, 8, 7, 6.5, 9, 9), BooleanBiFunction.OR);  // Holder
                return shape;
//                return Block.createCuboidShape(0, 0, 5, 5, 16, 11);
            case WEST:
                shape = Block.createCuboidShape(10, 4, 7.5, 16, 5, 8.5);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(10, 5, 7.5, 11, 8, 8.5), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(8.5, 7.999, 6, 12.5, 8.001, 10), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(9.5, 8, 7, 11.5, 9, 9), BooleanBiFunction.OR);  // Holder
                return shape;
            default:
                return Block.createCuboidShape(5, 0, 11, 11, 16, 16);
        }
    }
}
