package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public non-sealed interface RustableCandlestickLogic extends BaseCandlestickLogic {
    BlockState getRustPreviousLevel();
    BlockState getRustNextLevel();
    void setRustPreviousLevel(BlockState rustPreviousLevel);
    void setRustNextLevel(BlockState rustNextLevel);

    default boolean onUseRustableLogic(
            BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
            double x, double y, double z, double randomSpread
    ) {
        ItemStack heldItemStack = player.getItemInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToWax(state, world, pos, player, heldItemStack, heldItem, x, y, z, randomSpread)) return true;
        return tryToDeWaxOrDeRust(state, world, pos, player, hand, heldItemStack, heldItem, x, y, z, randomSpread);
    }

    default boolean tryToWax(
            BlockState state, Level world, BlockPos pos, Player player, ItemStack heldItemStack, Item heldItem,
            double x, double y, double z, double randomSpread
    ) {
        if (heldItem instanceof HoneycombItem && !state.getValue(ModProperties.WAXED)) {
            world.setBlockAndUpdate(pos, state.setValue(ModProperties.WAXED, true));
            if (!player.isCreative()) heldItemStack.shrink(1);
            emmitWaxOnParticles(world, x, y, z, randomSpread);
            SoundUtils.playSound(world, player, x, y, z, SoundEvents.HONEYCOMB_WAX_ON);
            return true;
        }
        return false;
    }
    default boolean tryToDeWaxOrDeRust(
            BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack heldItemStack, Item heldItem,
            double x, double y, double z, double randomSpread
    ) {
        if (heldItem instanceof AxeItem) {
            // De-wax
            if (state.getValue(ModProperties.WAXED)) {
                world.setBlockAndUpdate(pos, state.setValue(ModProperties.WAXED, false));
                damageItem(heldItemStack, player, hand);
                emmitWaxOffParticles(world, x, y, z, randomSpread);
                SoundUtils.playSound(world, player, x, y, z, SoundEvents.AXE_WAX_OFF);
                return true;
            }
            // De-rust
            else if (getRustPreviousLevel() != null) {
                damageItem(heldItemStack, player, hand);
                // ServerWorld check is required to be spam proof
                if (!world.isClientSide()) derust(state, world, pos);  // isClient() is required past 1.21.11
                // TODO: Check it still works in older versions and remove the comment
                return true;
            }
        }
        return false;
    }

    BlockState getChangedBlockState(BlockState newBase, BlockState state);
    default BlockState getChangedBlockStateUniversal(BlockState newBase, BlockState state) {
        return newBase
                .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED))
                .setValue(ModProperties.CANDLE_COLOR, state.getValue(ModProperties.CANDLE_COLOR))
                .setValue(BlockStateProperties.LIT, state.getValue(BlockStateProperties.LIT))
                .setValue(ModProperties.WAXED, state.getValue(ModProperties.WAXED));
    }
    default void rust(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, getChangedBlockState(getRustNextLevel(), state));
    }
    default void derust(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, getChangedBlockState(getRustPreviousLevel(), state));
    }

    default void emmitWaxingParticles(
            Level world,
            #if MC_VERSION < 12006
            SimpleParticleType particle,
            #else
            ParticleEffect particle,
            #endif
            double x, double y, double z,
            double randomSpread
    ) {
        RandomSource random = world.getRandom();

        for (int i = 0; i < 5; i++) {
            double randomX = x + ((random.nextDouble() - 0.5) * randomSpread);
            double randomY = y + ((random.nextDouble() - 0.5) * randomSpread);
            double randomZ = z + ((random.nextDouble() - 0.5) * randomSpread);
            world.addParticle(
                    particle, randomX, randomY, randomZ, 0, 0, 0
            );
        }
    }

    default void emmitWaxOnParticles(Level world, double offsetX, double offsetY, double offsetZ, double randomSpread) {
        emmitWaxingParticles(world, ParticleTypes.WAX_ON, offsetX, offsetY, offsetZ, randomSpread);
    }
    default void emmitWaxOffParticles(Level world, double offsetX, double offsetY, double offsetZ, double randomSpread) {
        emmitWaxingParticles(world, ParticleTypes.WAX_OFF, offsetX, offsetY, offsetZ, randomSpread);
    }
}
