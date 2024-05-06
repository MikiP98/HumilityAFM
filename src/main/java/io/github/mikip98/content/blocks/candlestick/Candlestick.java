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
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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

    protected void emmitWaxingParticles(BlockState state, World world, BlockPos pos, DefaultParticleType particleType) {
        Random random = world.random;

        double x = pos.getX();
        double y = pos.getY() + 0.5;
        double z = pos.getZ();

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

        for (int i = 0; i < 5; i++) {
            double randomX = x + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomY = y + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomZ = z + ((random.nextDouble() - 0.5) * 2 / 3);
            world.addParticle(particleType, randomX, randomY, randomZ, 0.0D, 0.0D, 0.0D);
//            world.addParticle(ParticleTypes.WAX_ON, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void emmitWaxOnParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_ON);
    }

    protected void emmitWaxOffParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_OFF);
    }

    protected String derusting(String metal) {
        return switch (metal) {
            case "oxidized_copper" -> "weathered_copper";
            case "weathered_copper" -> "exposed_copper";
            case "exposed_copper" -> "copper";
            default -> throw new IllegalStateException("Unexpected value: " + metal + ", for derusting() method.");
        };
    }
    protected String rust(String metal) {
        return switch (metal) {
            case "copper" -> "exposed_copper";
            case "exposed_copper" -> "weathered_copper";
            case "weathered_copper" -> "oxidized_copper";
            default -> throw new IllegalStateException("Unexpected value: " + metal + ", for rust() method.");
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
//        LOGGER.info(heldItem.getItem().getTranslationKey());

        final String[] parts = heldItem.getItem().getTranslationKey().split("\\.");
        if (parts[2].endsWith("candle") && parts[1].equals("minecraft")) {
            // Replace the candlestick block with the version with the candle
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
//            LOGGER.info("translationWords: " + Arrays.toString(translationWords));
            StringBuilder metal = new StringBuilder(translationWords[1]);
            for (int i = 2; i < translationWords.length; i++) {
                metal.append("_");
                metal.append(translationWords[i]);
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

        // If player has wax (honeycomb) in hand, use it and replace the candlestick with the waxed version
        } else if (player.getStackInHand(hand).getItem() == Items.HONEYCOMB) {
                String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
                if (!translationWords[1].equals("gold") && !translationWords[1].equals("waxed")) {

//                LOGGER.info("translationWords: " + Arrays.toString(translationWords));
                    StringBuilder newName = new StringBuilder("waxed");
                    for (int i = 1; i < translationWords.length; i++) {
                        newName.append("_");
                        newName.append(translationWords[i]);
                    }
//                LOGGER.info("newName: " + newName);
//                LOGGER.info("candlestickVariantsMap: " + CandlestickHelper.candlestickVariantsMap.toString());
                    emmitWaxOnParticles(state, world, pos);
                    world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                    BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newName.toString()).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
                    world.setBlockState(pos, newState);
                    return ActionResult.SUCCESS;
                }
        // If player is holding an axe
        } else if (player.getStackInHand(hand).getItem() instanceof net.minecraft.item.AxeItem) {
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            // check if candlestick is waxed
            if (translationWords[1].equals("waxed")) {
                StringBuilder newName = new StringBuilder(translationWords[2]);
                for (int i = 3; i < translationWords.length; i++) {
                    newName.append("_");
                    newName.append(translationWords[i]);
                }
                // Remove the wax from the candlestick
                BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newName.toString()).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
                world.setBlockState(pos, newState);
                emmitWaxOffParticles(state, world, pos);
                world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return ActionResult.SUCCESS;

            // else if not copper is in an oxidation state, go 1 stage back
            } else {
                StringBuilder metal = new StringBuilder(translationWords[1]);
                for (int i = 2; i < translationWords.length; i++) {
                    if (translationWords[i].equals("candle"))
                        break;
                    metal.append("_");
                    metal.append(translationWords[i]);
                }
                String metalS = metal.toString();
                if (!(metalS.equals("copper") || metalS.equals("gold"))) {
                    String newMetal = derusting(metalS);
                    BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newMetal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
                    world.setBlockState(pos, newState);
                    emmitWaxingParticles(state, world, pos, ParticleTypes.SCRAPE);
                    world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                    world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);

                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.random.nextInt(29999) == 0) {
            // Rust the copper
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (!(metal.equals("waxed") || state.getBlock() instanceof CandlestickWithCandle)) {
                for (int i = 2; i < translationWords.length; i++) {
                    metal += "_";
                    metal += translationWords[i];
                }
                if (!(metal.equals("oxidized_copper") || metal.equals("gold"))) {
                    String newMetal = rust(metal);
                    BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newMetal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
                    world.setBlockState(pos, newState);
                }
            }
        }
        super.randomDisplayTick(state, world, pos, random);
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
