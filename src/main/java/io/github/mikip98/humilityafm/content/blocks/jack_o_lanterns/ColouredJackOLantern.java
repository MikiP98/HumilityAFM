package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColouredJackOLantern extends JackOLantern {
    public static final IntProperty POWER = Properties.POWER;

    public ColouredJackOLantern() { this(defaultSettings); }
    public ColouredJackOLantern(Settings settings) {
        super(settings.luminance((state) -> state.get(POWER)));
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
            SoundUtils.playSoundAtBlockCenter(world, pos, SoundEvents.BLOCK_PUMPKIN_CARVE);
            world.setBlockState(pos, state.with(POWER, currentPower - 3));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
