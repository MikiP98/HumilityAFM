package io.github.mikip98.content.blocks.candlestick;

import io.github.mikip98.helpers.CandlestickHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
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

    private ItemStack candleFromString(String candle_name) {
        return switch (candle_name) {
            case "candle" -> new ItemStack(Items.CANDLE);
            case "white" -> new ItemStack(Items.WHITE_CANDLE);
            case "orange" -> new ItemStack(Items.ORANGE_CANDLE);
            case "magenta" -> new ItemStack(Items.MAGENTA_CANDLE);
            case "light_blue" -> new ItemStack(Items.LIGHT_BLUE_CANDLE);
            case "yellow" -> new ItemStack(Items.YELLOW_CANDLE);
            case "lime" -> new ItemStack(Items.LIME_CANDLE);
            case "pink" -> new ItemStack(Items.PINK_CANDLE);
            case "gray" -> new ItemStack(Items.GRAY_CANDLE);
            case "light_gray" -> new ItemStack(Items.LIGHT_GRAY_CANDLE);
            case "cyan" -> new ItemStack(Items.CYAN_CANDLE);
            case "purple" -> new ItemStack(Items.PURPLE_CANDLE);
            case "blue" -> new ItemStack(Items.BLUE_CANDLE);
            case "brown" -> new ItemStack(Items.BROWN_CANDLE);
            case "green" -> new ItemStack(Items.GREEN_CANDLE);
            case "red" -> new ItemStack(Items.RED_CANDLE);
            case "black" -> new ItemStack(Items.BLACK_CANDLE);
            default -> throw new IllegalStateException("Unexpected value: " + candle_name + ", for candle name");
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // If player has wax (honeycomb) in hand, use it and replace the candlestick with the waxed version
        if (player.getStackInHand(hand).getItem() == Items.HONEYCOMB) {
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            if (!translationWords[1].equals("gold") && !translationWords[1].equals("waxed")) {

//                LOGGER.info("translationWords: " + Arrays.toString(translationWords));
                StringBuilder newName = new StringBuilder("waxed");
                for (int i = 1; i < translationWords.length; i++) {
                    if (translationWords[i].equals("candle"))
                        continue;
                    newName.append("_");
                    newName.append(translationWords[i]);
                }
                newName.append("_candle");
//                LOGGER.info("newName: " + newName);
//                LOGGER.info("candlestickVariantsMap: " + CandlestickHelper.candlestickVariantsMap.toString());
                emmitWaxOnParticles(state, world, pos);
                world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newName.toString()).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(LIT, state.get(LIT));
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
                    if (translationWords[i].equals("candle"))
                        continue;
                    newName.append("_");
                    newName.append(translationWords[i]);
                }
                newName.append("_candle");
                // Remove the wax from the candlestick
                BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newName.toString()).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(LIT, state.get(LIT));
                world.setBlockState(pos, newState);
                emmitWaxOffParticles(state, world, pos);
                world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return ActionResult.SUCCESS;
            // else if not copper is in an oxidation state, go 1 stage back
            } else {
                LOGGER.info("translationWords: " + Arrays.toString(translationWords));
                StringBuilder metal = new StringBuilder(translationWords[1]);
                int metalEndIndex = 2;
                for (int i = 2; i < translationWords.length; i++) {
                    if (translationWords[i].equals("candle")) {
                        metalEndIndex = i;
                        break;
                        }
                    metal.append("_");
                    metal.append(translationWords[i]);
                }
                String metalS = metal.toString();
                LOGGER.info("metalS: " + metalS);

                if (!(metalS.equals("copper") || metalS.equals("gold"))) {
                    String newMetal = derusting(metalS);

                    for (int i = metalEndIndex + 1; i < translationWords.length; i++) {
                        newMetal += "_";
                        newMetal += translationWords[i];
                    }
                    newMetal += "_candle";
                    LOGGER.info("newMetal: " + newMetal);

                    BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newMetal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(LIT, state.get(LIT));
                    world.setBlockState(pos, newState);
                    emmitWaxingParticles(state, world, pos, ParticleTypes.SCRAPE);
                    world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                    world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);

                    return ActionResult.SUCCESS;
                }
            }

        } else if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL) {
            world.setBlockState(pos, state.with(LIT, true));
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            randomDisplayTick(state, world, pos, world.random); // Emit particles
            // Consume the flint and steel (decrease the durability by 1)
            player.getStackInHand(hand).damage(1, player, (p) -> p.sendToolBreakStatus(hand));
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
            // Give back the candle item to the player
            // (if the player has enough space in the inventory, else drop it on the ground)
//            LOGGER.info("translationWords: " + Arrays.toString(translationWords));
            String candle_name = translationWords[translationWords.length - 2];
            if (candle_name.equals("light") || candle_name.equals("dark")) {
                candle_name += "_" + translationWords[translationWords.length - 1];
            } else {
                candle_name = translationWords[translationWords.length - 1];
            }
//            LOGGER.info("candle_name: " + candle_name);
            ItemStack candleItemStack = candleFromString(candle_name);
            if (!player.getInventory().insertStack(candleItemStack)) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), candleItemStack));
            }

            // Remove candle from candlestick
            BlockState newState = CandlestickHelper.candlestickVariantsMap.get(metal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
            world.setBlockState(pos, newState);
            if (world.random.nextInt(99) == 0) world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 12.0f, 0.5f, true);
            else world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.95f, 1.0f, true);
            return ActionResult.SUCCESS;
        }
        if (state.get(LIT)) {
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
        if (world.random.nextInt(29999) == 0) {
            // Rust the copper
            String[] translationWords = state.getBlock().getTranslationKey().split("\\.")[2].split("_");
            String metal = translationWords[1];
            if (!metal.equals("waxed")) {
                int metalEndIndex = 2;
                for (int i = 2; i < translationWords.length - 1; i++) {
                    if (translationWords[i].equals("candle")) {
                        metalEndIndex = i;
                        break;
                    }
                    metal += "_" + translationWords[i];
                }
                if (!(metal.equals("oxidized_copper") || metal.equals("gold"))) {
                    String newMetal = rust(metal);
                    for (int i = metalEndIndex + 1; i < translationWords.length - 1; i++) {
                        newMetal += "_" + translationWords[i];
                    }
                    newMetal += "_candle";
                    BlockState newState = CandlestickHelper.candlestickVariantsMap.get(newMetal).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(LIT, state.get(LIT));
                    world.setBlockState(pos, newState);
                    return;
                }
            }
        }
        super.randomDisplayTick(state, world, pos, random);
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
