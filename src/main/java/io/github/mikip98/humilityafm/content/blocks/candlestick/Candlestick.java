package io.github.mikip98.humilityafm.content.blocks.candlestick;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.util.data_types.CandleColor;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Candlestick extends HorizontalFacingBlock implements Waterloggable {
    protected static final VoxelShape voxelShapeEmptyNorth = VoxelShapes.union(
            Block.createCuboidShape(7.5, 4, 10, 8.5, 5, 16),
            Block.createCuboidShape(7.5, 5, 10, 8.5, 8, 11),
            Block.createCuboidShape(6, 7.999, 8.5, 10, 8.001, 12.5),  // Dripper
            Block.createCuboidShape(7, 8, 9.5, 9, 9, 11.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptySouth = VoxelShapes.union(
            Block.createCuboidShape(7.5, 4, 0, 8.5, 5, 6),
            Block.createCuboidShape(7.5, 5, 5, 8.5, 8, 6),
            Block.createCuboidShape(6, 7.999, 3.5, 10, 8.001, 7.5),  // Dripper
            Block.createCuboidShape(7, 8, 4.5, 9, 9, 6.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyEast = VoxelShapes.union(
            Block.createCuboidShape(0, 4, 7.5, 6, 5, 8.5),
            Block.createCuboidShape(5, 5, 7.5, 6, 8, 8.5),
            Block.createCuboidShape(3.5, 7.999, 6, 7.5, 8.001, 10),  // Dripper
            Block.createCuboidShape(4.5, 8, 7, 6.5, 9, 9)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyWest = VoxelShapes.union(
            Block.createCuboidShape(10, 4, 7.5, 16, 5, 8.5),
            Block.createCuboidShape(10, 5, 7.5, 11, 8, 8.5),
            Block.createCuboidShape(8.5, 7.999, 6, 12.5, 8.001, 10),  // Dripper
            Block.createCuboidShape(9.5, 8, 7, 11.5, 9, 9)  // Holder
    );

    protected static final VoxelShape voxelShapeCandleNorth = VoxelShapes.union(
            voxelShapeEmptyNorth,
            Block.createCuboidShape(7, 9.0001, 9.5, 9, 11, 11.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleSouth = VoxelShapes.union(
            voxelShapeEmptySouth,
            Block.createCuboidShape(7, 9.0001, 4.5, 9, 11, 6.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleEast = VoxelShapes.union(
            voxelShapeEmptyEast,
            Block.createCuboidShape(4.5, 9.0001, 7, 6.5, 11, 9)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleWest = VoxelShapes.union(
            voxelShapeEmptyWest,
            Block.createCuboidShape(9.5, 9.0001, 7, 11.5, 11, 9)
    );  // Empty + Candle


    public static Settings getDefaultSettings() {
        return Settings.create()
                .strength(0.5f)
                .requiresTool()
                .nonOpaque()
                .sounds(BlockSoundGroup.METAL)
                .luminance(state -> state.get(Properties.LIT) ? 4 : 0);
    }
    public static final Settings defaultSettings = getDefaultSettings();

    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

    protected static final MapCodec<Candlestick> CODEC = createCodec(Candlestick::new);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(Properties.WATERLOGGED);
        builder.add(ModProperties.CANDLE);
        builder.add(CANDLE_COLOR);
        builder.add(Properties.LIT);
    }

    public Candlestick(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.SOUTH)
                .with(Properties.WATERLOGGED, false)
                .with(ModProperties.CANDLE, false)
                .with(CANDLE_COLOR, CandleColor.PLAIN)
                .with(Properties.LIT, false)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public Candlestick(Settings settings, boolean ignored) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    // TODO: Move each event to a separate function
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        ItemStack heldItem = player.getStackInHand(hand);

        // Insert candle
        if (heldItem.getItem() instanceof BlockItem) {
            if (((BlockItem) heldItem.getItem()).getBlock() instanceof CandleBlock) {
                // Check if the candlestick is already holding a candle
                if (state.get(ModProperties.CANDLE)) {
                    player.getInventory().offerOrDrop(new ItemStack(state.get(CANDLE_COLOR).asCandle()));
                }
                world.setBlockState(pos, state.with(ModProperties.CANDLE, true).with(CANDLE_COLOR, CandleColor.getColor(heldItem.getItem())), Block.NOTIFY_ALL);
                if (!player.isCreative()) heldItem.decrement(1);
                world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.9f, 1.1f, true);
                return ActionResult.SUCCESS;
            }
        }
        // Remove candle and extinguish
        if (heldItem.isEmpty() && player.isSneaking()) {
            // Extinguish the candle
            if (state.get(Properties.LIT)) {
                world.setBlockState(pos, state.with(Properties.LIT, false), Block.NOTIFY_ALL);
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return ActionResult.SUCCESS;
            }
            // Remove the candle
            else if (state.get(ModProperties.CANDLE)) {
                player.getInventory().offerOrDrop(new ItemStack(state.get(CANDLE_COLOR).asCandle()));
                world.setBlockState(pos, state.with(ModProperties.CANDLE, false), Block.NOTIFY_ALL);
                world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.9f, 0.9f, true);
                return ActionResult.SUCCESS;
            }
        }
        // Light the candle
        if (heldItem.getItem() instanceof FlintAndSteelItem && state.get(ModProperties.CANDLE) && !state.get(Properties.LIT) && !state.get(Properties.WATERLOGGED)) {
            world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL);
            if (!player.isCreative() && !world.isClient) {
                heldItem.damage(
                        1,
                        (ServerWorld) world,
                        (ServerPlayerEntity) player,
                        item -> player.sendEquipmentBreakStatus(item, LivingEntity.getSlotForHand(hand))
                );
            }
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Check if the block is lit (property LIT is true)
        if (state.get(Properties.LIT)) {
            double x = pos.getX() + 0.5;// + this.offsetX;
            double y = pos.getY() + 0.8;// + this.offsetY;
            double z = pos.getZ() + 0.5;// + this.offsetZ;
            if (random.nextInt(1) == 0) {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        z += 0.15;
                        break;
                    case SOUTH:
                        z -= 0.15;
                        break;
                    case EAST:
                        x -= 0.15;
                        break;
                    case WEST:
                        x += 0.15;
                }
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
        super.randomDisplayTick(state, world, pos, random);
    }
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, boolean ignored) {
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        if (state.get(ModProperties.CANDLE)) {
            switch (dir) {
                case NORTH -> {
                    return voxelShapeCandleNorth;
                }
                case SOUTH -> {
                    return voxelShapeCandleSouth;
                }
                case EAST -> {
                    return voxelShapeCandleEast;
                }
                case WEST -> {
                    return voxelShapeCandleWest;
                }
            }
        } else {
            switch (dir) {
                case NORTH -> {
                    return voxelShapeEmptyNorth;
                }
                case SOUTH -> {
                    return voxelShapeEmptySouth;
                }
                case EAST -> {
                    return voxelShapeEmptyEast;
                }
                case WEST -> {
                    return voxelShapeEmptyWest;
                }
            }
        }
        return null;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && state.get(ModProperties.CANDLE)) {
            Block.dropStack(world, pos, new ItemStack(state.get(CANDLE_COLOR).asCandle()));
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }
}
