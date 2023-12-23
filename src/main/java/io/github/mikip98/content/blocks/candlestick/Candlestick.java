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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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

        if (heldItem.getItem() == Items.CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Decrement candle amount in player's inventory
            if (!player.isCreative()) {
                heldItem.decrement(1);
            }

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.WHITE_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "white_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.ORANGE_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "orange_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.MAGENTA_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "magenta_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.LIGHT_BLUE_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "light_blue_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.YELLOW_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "yellow_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.LIME_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "lime_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.PINK_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "pink_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.GRAY_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "gray_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.LIGHT_GRAY_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "light_gray_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.CYAN_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "cyan_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.PURPLE_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "purple_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.BLUE_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "blue_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.BROWN_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "brown_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.GREEN_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "green_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.RED_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "red_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        } else if (heldItem.getItem() == Items.BLACK_CANDLE) {
            // Replace the candlestick block with the version with the candle
            String candle = "black_candle";
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (translationWords.length == 3) {
                metal += "_" + translationWords[2];
            }
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal + "_" + candle).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);

            // Return success to indicate the interaction was successful
            return ActionResult.SUCCESS;
        }

        // If the conditions are not met, return pass to allow other interactions
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

//    @Override
//    public void Tick(BlockState state, World world, BlockPos pos, Random random) {
//        if (state.get(LIT)) {
//            world.setBlockState(pos, state.with(LIT, false));
//        }
//    }

//    @Override
//    public boolean randomDisplayTick(BlockState state) {
//        return true;
//    }
}
