package io.github.mikip98.humilityafm.content.blocks.coloured_torch;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.codecs.RecordCodecBuilder; #endif
#if POLYMER import eu.pb4.polymer.core.api.block.PolymerBlock; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerBlockEntities; #endif
import io.github.mikip98.humilityafm.util.SoundUtils;
#if POLYMER && MC_VERSION >= 260000 import net.fabricmc.fabric.api.networking.v1.context.PacketContext; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
#if POLYMER import net.minecraft.world.level.block.EntityBlock; #endif
import net.minecraft.world.level.block.TorchBlock;
#if POLYMER import net.minecraft.world.level.block.entity.BlockEntity; #endif
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
#if POLYMER && MC_VERSION >= 12104 && MC_VERSION < 260000 import xyz.nucleoid.packettweaker.PacketContext; #endif

public class ColouredTorch extends TorchBlock #if POLYMER implements EntityBlock, PolymerBlock #endif {
    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<ColouredTorch> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            PARTICLE_OPTIONS_FIELD.forGetter((torchBlock) -> torchBlock.flameParticle),
            propertiesCodec()
    ).apply(instance, ColouredTorch::new));

    @Override
    public @NotNull MapCodec<? extends ColouredTorch> codec() {
        return CODEC;
    }
    #endif

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final Properties defaultSettings = Properties.ofFullCopy(Blocks.TORCH)
            .lightLevel((state) -> state.getValue(POWER));


    public ColouredTorch(SimpleParticleType particleType, Properties properties) {
        // super(particleOptions: particleType, properties: properties);  // Crashes the compiler, report to Manifold
        #if MC_VERSION < 12004
        super(properties, particleType);
        #else
        super(particleType, properties);
        #endif
        registerDefaultState(defaultBlockState().setValue(POWER, 15));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }

    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        if (onUseLogicInternal(state, level, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.use(state, level, pos, player, hand, hit);
    }
    #else
    public @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull BlockHitResult hit
    ) {
        final InteractionHand hand = player.getUsedItemHand();
        if (onUseLogicInternal(state, level, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.useWithoutItem(state, level, pos, player, hit);
    }
    #endif
    // TODO: Maybe an interface in HumilityAFM that will take over the use() method and run it's own useWithItem and useWithoutItem?

    protected static boolean onUseLogicInternal(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        final int currentPower = state.getValue(POWER);
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty() && currentPower > 3) {
            SoundUtils.playSoundAtBlockCenter(level, player, pos, SoundEvents.CANDLE_EXTINGUISH, 1.2f, 0.75f);
            level.setBlockAndUpdate(pos, state.setValue(POWER, currentPower - 3));
            return true;
        }
        return false;
    }

    #if POLYMER
    #if MC_VERSION < 12006
    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.TORCH;
    }
    #endif
    @Override
    public BlockState getPolymerBlockState(BlockState state #if MC_VERSION >= 12104, PacketContext context #endif) {
        return Blocks.TORCH.defaultBlockState();
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PolymerBlockEntities.ColouredTorchBlockEntity(pos, state);
    }
    #endif
}
