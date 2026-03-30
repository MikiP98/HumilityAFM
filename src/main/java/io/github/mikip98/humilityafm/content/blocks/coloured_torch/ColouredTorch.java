package io.github.mikip98.humilityafm.content.blocks.coloured_torch;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;

public class ColouredTorch extends TorchBlock {
    public static final IntProperty POWER = Properties.POWER;

    public static final Properties defaultSettings = Properties.copy(Blocks.TORCH).luminance((state) -> state.getValue(POWER));

    #if MC_VERSION == 12001
    public ColouredTorch(ParticleEffect particle, Properties settings) {
        super(settings, particle);
        registerDefaultState(defaultBlockState().setValue(POWER, 15));
    }
    #elif MC_VERSION == 12004
    public ColouredTorch(DefaultParticleType particleType, Settings settings) {
        super(particleType, settings);
        setDefaultState(getDefaultState().with(POWER, 15));
    }
    #else
     public ColouredTorch(SimpleParticleType particleType, Settings settings) {
        super(particleType, settings);
        setDefaultState(getDefaultState().with(POWER, 15));
    }
    #endif


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWER);
    }

    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    #else
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        final Hand hand = player.getActiveHand();
    #endif
        final int currentPower = state.get(POWER);
        if (player.isSneaking() && player.getStackInHand(hand).isEmpty() && currentPower > 3) {
            SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, 1.2f, 0.75f);
            world.setBlockState(pos, state.with(POWER, currentPower - 3));
            return ActionResultWrapper.SUCCESS;
        }
        #if MC_VERSION < 12006
        return super.onUse(state, world, pos, player, hand, hit);
        #else
        return super.onUse(state, world, pos, player, hit);
        #endif
    }
}
