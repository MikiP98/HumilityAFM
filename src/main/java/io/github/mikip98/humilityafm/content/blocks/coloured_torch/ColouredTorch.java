package io.github.mikip98.humilityafm.content.blocks.coloured_torch;

import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColouredTorch extends TorchBlock {
    public static final IntProperty POWER = Properties.POWER;

    public static final Settings defaultSettings = Settings.copy(Blocks.TORCH);
    public ColouredTorch(DefaultParticleType particle) { this(defaultSettings, particle); }
    public ColouredTorch(Settings settings, DefaultParticleType particle) {
        super(particle, settings.luminance((state) -> state.get(POWER)));
        setDefaultState(getDefaultState().with(POWER, 15));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final int currentPower = state.get(POWER);
        if (player.isSneaking() && player.getStackInHand(hand).isEmpty() && currentPower > 3) {
            SoundUtils.playSoundAtBlockCenter(world, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, 2.5f, -0.25f);
            world.setBlockState(pos, state.with(POWER, currentPower - 3));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
