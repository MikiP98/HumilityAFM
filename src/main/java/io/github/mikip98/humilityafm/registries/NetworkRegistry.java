package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
#if MC_VERSION < 12006 import net.fabricmc.fabric.api.networking.v1.PacketByteBufs; #endif
#if MC_VERSION >= 12006 import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry; #endif
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.FriendlyByteBuf;
#if MC_VERSION >= 12006 import net.minecraft.network.codec.StreamCodec; #endif
#if MC_VERSION >= 12006 import net.minecraft.network.protocol.common.custom.CustomPacketPayload; #endif
#if MC_VERSION < 12006 import net.minecraft.resources.ResourceLocation; #endif

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class NetworkRegistry {
    #if MC_VERSION >= 12006
    public static void registerPayload() {
        PayloadTypeRegistry #if MC_VERSION < 260000 .playS2C() #else .clientboundPlay() #endif
                .register(ConfigSyncPayload.TYPE, ConfigSyncPayload.CODEC);
    }
    #else
    public static final ResourceLocation CONFIG_SYNC = getId("config_sync");
    #endif

    @Environment(EnvType.SERVER)
    public static void registerNetworkServerMessage() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            byte[] modSupport = new byte[SupportedMods.values().length - 1];
            int i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                modSupport[i] = (byte) ModConfig.modSupport.get(mod).ordinal();
                ++i;
            }
            final ConfigSyncPayload payload = new ConfigSyncPayload(
                    ModConfig.getEnableCandlestickBeta(),
                    ModConfig.getEnableColouredFeatureSetBeta(),
                    ModConfig.mosaicsAndTilesStrengthMultiplayer,
                    modSupport
            );
            #if MC_VERSION < 12006
            sender.sendPacket(CONFIG_SYNC, payload.save());
            #else
            sender.sendPacket(payload);
            #endif
        });
    }

    public record ConfigSyncPayload(
            boolean enableCandlestick,
            boolean enableColouredFeature,
            float mosaicsStrength,
            byte[] modSupport
    )#if MC_VERSION >= 12006 implements CustomPacketPayload #endif{

        #if MC_VERSION >= 12006
        public static final CustomPacketPayload.Type<ConfigSyncPayload> TYPE = new CustomPacketPayload.Type<>(getId("config_sync"));

        public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> CODEC =
                StreamCodec.ofMember(ConfigSyncPayload::write, ConfigSyncPayload::new);
        #endif

        public ConfigSyncPayload(FriendlyByteBuf buf) {
            this(
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readFloat(),
                    buf.readByteArray()
            );
        }
        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(enableCandlestick);
            buf.writeBoolean(enableColouredFeature);
            buf.writeFloat(mosaicsStrength);
            buf.writeByteArray(modSupport);
        }
        #if MC_VERSION < 12006
        public FriendlyByteBuf save() {
            FriendlyByteBuf buf = PacketByteBufs.create();
            write(buf);
            return buf;
        }
        #endif

        #if MC_VERSION >= 12006
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
        #endif
    }
}
