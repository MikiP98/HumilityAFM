package io.github.mikip98.content.blocks.candlestick;

import io.github.mikip98.helpers.CandlestickHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

import java.util.Arrays;

import static io.github.mikip98.HumilityAFM.LOGGER;

public class CandlestickWithCandle extends Candlestick{
    public static final BooleanProperty LIT = Properties.LIT;

//    private boolean calculatedOffset = false;
//    private double offsetX = 0.5;
//    private final double offsetY = 0.85;
//    private double offsetZ = 0.5;

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
            world.setBlockState(pos, state.with(LIT, true));
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            // TODO: Consume the flint and steel (decrease the durability by 1)
            return ActionResult.SUCCESS;
        } else if (player.getStackInHand(hand).isEmpty() && player.isSneaking()) {
            if (state.get(LIT)) {
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            }
            // Replace the candlestick block with the version without the candle
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            // E.G oxidized_copper_light_blue_candlestick -> oxidized_copper
            String metal = translationWords[1];
//            for (int i = 1; i < translationWords.length - 1; i++) {
//                metal += "_" + translationWords[i];
//                if (Arrays.stream(CandlestickHelper.metals).toList().contains(translationWords[i]))
//                    break;
//            }
            if (translationWords.length > 3) {
                if (!Arrays.stream(CandlestickHelper.metals).toList().contains(metal)) {
                    for (int i = 2; i < translationWords.length - 1; i++) {
                        metal += "_" + translationWords[i];
                        if (Arrays.stream(CandlestickHelper.metals).toList().contains(translationWords[i]))
                            break;
                    }
                }
            }
//            LOGGER.info("metal: " + metal);
            // TODO: Give back the candle item to the player
            //  (if the player has enough space in the inventory, else drop it on the ground)
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);
            if (world.random.nextInt(99) == 0) world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 12.0f, 0.5f, true);
            else world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.95f, 1.0f, true);
            return ActionResult.SUCCESS;

        } else if (state.get(LIT)) {
            if (!(player.getStackInHand(hand).getItem() instanceof BlockItem)) {
                world.setBlockState(pos, state.with(LIT, false));
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return ActionResult.SUCCESS;
            }
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
            if (random.nextInt(1) == 0) {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        z += 0.65;
                        x += 0.5;
                        break;
                    case SOUTH:
                        z += 0.35;
                        x += 0.5;
                        break;
                    case EAST:
                        x += 0.35;
                        z += 0.5;
                        break;
                    case WEST:
                        x += 0.65;
                        z += 0.5;
                        break;
                    default:
                        x += 0.5;
                        z += 0.5;
                }
                y += 0.80;
                float velocityPlaneMultiplayer = 0.001953125f;
                double velocityY = random.nextDouble() * 0.001953125;
                double velocityX = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                double velocityZ = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                world.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, velocityX, velocityY, velocityZ);

                if (random.nextInt(4) == 0) {
                    velocityPlaneMultiplayer = 0.00390625f;
                    velocityY = random.nextDouble() * 0.00390625;
                    velocityX = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                    velocityZ = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, velocityX, velocityY, velocityZ);
                }
            }
            if (random.nextInt(4) == 0) {
                world.playSound(x, y, z, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            }
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
            case EAST:
                shape = Block.createCuboidShape(0, 4, 7.5, 6, 5, 8.5);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(5, 5, 7.5, 6, 8, 8.5), BooleanBiFunction.OR);
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(3.5, 7.999, 6, 7.5, 8.001, 10), BooleanBiFunction.OR); // Dripper
                shape = VoxelShapes.combineAndSimplify(shape, Block.createCuboidShape(4.5, 8, 7, 6.5, 11, 9), BooleanBiFunction.OR);  // Holder & Candle
                return shape;
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
